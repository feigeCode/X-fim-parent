package com.feige.api.serialize;

import com.feige.api.annotation.MsgComp;
import com.feige.api.msg.Msg;
import com.feige.fim.utils.AssertUtil;
import com.feige.fim.utils.ReflectionUtils;
import com.feige.framework.annotation.InitMethod;
import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.ApplicationContextAware;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public abstract class AbstractSerializedClassManager implements SerializedClassManager, ApplicationContextAware {
    
    protected final Map<Byte, Serializer> serializerMap = new ConcurrentHashMap<>();
    protected final Map<String, Class<?>> classMap = new ConcurrentHashMap<>();
    protected final Map<Class<?>, Byte> classKeyMap = new ConcurrentHashMap<>();
    protected final Map<Byte, SerializedClassGenerator> generatorMap = new ConcurrentHashMap<>();

    protected ApplicationContext applicationContext;

    @InitMethod
    public void initializeSerializer(){
        List<Serializer> serializers = applicationContext.getByType(Serializer.class);
        for (Serializer serializer : serializers) {
            this.register(serializer);
        }
    }

    @InitMethod
    public void initializeSerializedClassGenerator(){
        List<SerializedClassGenerator> serializedClassGenerators = applicationContext.getByType(SerializedClassGenerator.class);
        for (SerializedClassGenerator serializedClassGenerator : serializedClassGenerators) {
            this.registerClassGenerator(serializedClassGenerator);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void register(Serializer serializer) {
        serializerMap.put(serializer.getType(), serializer);
    }

    @Override
    public void registerClass(byte serializerType, byte classKey, Class<?> clazz) {
        classMap.put(joinClassKey(serializerType, classKey), clazz);
    }

    @Override
    public void registerClassGenerator(SerializedClassGenerator serializedClassGenerator) {
        generatorMap.put(serializedClassGenerator.getSerializerType(), serializedClassGenerator);
    }
    

    @Override
    public Serializer unregister(byte serializerType) {
        return serializerMap.remove(serializerType);
    }
    
    
    @Override
    public SerializedClassGenerator unregisterClassGenerator(byte serializerType) {
        return generatorMap.remove(serializerType);
    }
    
    @Override
    public Class<?> unregisterClass(byte serializerType, byte classKey) {
        return classMap.remove(joinClassKey(serializerType, classKey));
    }

    @Override
    public Serializer getSerializer(byte serializerType) {
        return serializerMap.get(serializerType);
    }
    
    
    @Override
    public SerializedClassGenerator getClassGenerator(byte serializerType) {
        return generatorMap.get(serializerType);
    }
    
    @Override
    public Class<?> getClass(byte serializerType, byte classKey) {
        return classMap.get(joinClassKey(serializerType, classKey));
    }

    @Override
    public <T extends Msg> Class<T> getClass(byte serializerType, Class<T> msgInterface, Supplier<Object[]> supplier) throws IllegalStateException {
        AssertUtil.notNull(msgInterface, "msg interface");
        Byte classKey = classKeyMap.get(msgInterface);
        if (classKey == null){
            if (!msgInterface.isAnnotationPresent(MsgComp.class)){
                throw new RuntimeException("[" + msgInterface.getCanonicalName() + "] not @MsgComp");
            }
            MsgComp msgComp = msgInterface.getAnnotation(MsgComp.class);
            classKey = msgComp.classKey();
            classKeyMap.put(msgInterface, classKey);
        }

        Class<?> realClass = getClass(serializerType, classKey);
        if (realClass == null){
            synchronized (this){
                realClass = getClass(serializerType, classKey);
                if (realClass == null){
                    SerializedClassGenerator classGenerator = getClassGenerator(serializerType);
                    try {
                        realClass = classGenerator.generate(msgInterface, supplier == null ? null : supplier.get());
                        registerClass(serializerType, classKey, realClass);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        AssertUtil.notNull(realClass, "generate class");
        return (Class<T>) realClass;
    }

    @Override
    public <T extends Msg> T newObject(byte serializerType, byte classKey) {
        Class<?> realClass = getClass(serializerType, classKey);
        AssertUtil.notNull(realClass, "generate class");
        Object instance;
        try {
            instance = ReflectionUtils.accessibleConstructor(realClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (T) instance;
    }

    @Override
    public <T extends Msg> T newObject(byte serializerType, Class<T> msgInterface, Supplier<Object[]> supplier) {
        Class<T> realClass = getClass(serializerType, msgInterface, supplier);
        Object instance;
        try {
            instance = ReflectionUtils.accessibleConstructor(realClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (T) instance;
    }
    
    


    @Override
    public Object getDeserializedObject(byte serializerType, byte classKey, byte[] bytes) {
        Serializer serializer = getSerializer(serializerType);
        Class<?> serializedClass = getClass(serializerType, classKey);
        if (serializer != null && serializedClass != null){
            return serializer.deserialize(serializedClass, bytes);
        }
        return null;
    }

    @Override
    public byte[] getSerializedObject(byte serializerType, Msg msg) {
        Serializer serializer = serializerMap.get(serializerType);
        if (serializer != null){
            return serializer.serialize(msg);
        }
        return null;
    }

    protected String joinClassKey(byte serializerType, byte classKey){
        return String.valueOf(serializerType) + "-" + String.valueOf(classKey);
    }
}
