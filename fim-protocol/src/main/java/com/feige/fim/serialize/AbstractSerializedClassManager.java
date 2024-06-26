package com.feige.fim.serialize;

import com.feige.api.annotation.MsgComp;
import com.feige.api.msg.Msg;
import com.feige.api.msg.MsgFactory;
import com.feige.api.serialize.ClassGen;
import com.feige.api.serialize.MsgGen;
import com.feige.api.serialize.SerializedClassGenerator;
import com.feige.api.serialize.SerializedClassManager;
import com.feige.api.serialize.Serializer;
import com.feige.utils.common.AssertUtil;
import com.feige.utils.clazz.ReflectionUtils;
import com.feige.framework.annotation.InitMethod;
import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.aware.ApplicationContextAware;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Setter
public abstract class AbstractSerializedClassManager implements SerializedClassManager, ApplicationContextAware {
    
    protected final Map<Byte, Serializer> serializerMap = new ConcurrentHashMap<>();
    protected final Map<String, Class<?>> classMap = new ConcurrentHashMap<>();
    protected final Map<Class<?>, Byte> classKeyMap = new ConcurrentHashMap<>();
    protected final Map<Byte, SerializedClassGenerator> generatorMap = new ConcurrentHashMap<>();
    protected final Map<String, MsgFactory> msgFactoryMap = new ConcurrentHashMap<>();

    protected ApplicationContext applicationContext;

    private List<SerializedClassGenerator> serializedClassGenerators;

    private List<Serializer> serializers;

    @InitMethod
    public void initializeSerializer(){
        for (SerializedClassGenerator serializedClassGenerator : serializedClassGenerators) {
            this.registerClassGenerator(serializedClassGenerator);
            this.classGen(serializedClassGenerator);
        }
        
        for (Serializer serializer : serializers) {
            this.register(serializer);
        }
    }
    
    private void classGen(SerializedClassGenerator serializedClassGenerator){
        List<ClassGen> classGens = serializedClassGenerator.getClassGen();
        for (ClassGen classGen : classGens) {
            Object[] args = classGen.getArgs();
            this.getClass(serializedClassGenerator.getSerializerType(), classGen.getMsgClass(), args == null || args.length == 0 ? null : () -> args);
        }
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
    public void registerMsgFactory(byte serializerType, byte classKey, MsgFactory msgFactory) {
        msgFactoryMap.put(joinClassKey(serializerType, classKey), msgFactory);
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
    public MsgFactory unregisterMsgFactory(byte serializerType, byte classKey) {
        return msgFactoryMap.get(joinClassKey(serializerType, classKey));
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
    public byte getClassKey(Class<?> msgInterface) {
        Byte classKey = classKeyMap.get(msgInterface);
        if (classKey == null){
            if (!msgInterface.isAnnotationPresent(MsgComp.class)){
                throw new RuntimeException("[" + msgInterface.getCanonicalName() + "] not @MsgComp");
            }
            MsgComp msgComp = msgInterface.getAnnotation(MsgComp.class);
            classKey = msgComp.classKey();
            classKeyMap.put(msgInterface, classKey);
        }
        return classKey;
    }

    @Override
    public Class<?> getClass(byte serializerType, byte classKey) {
        return classMap.get(joinClassKey(serializerType, classKey));
    }

    @Override
    public <T extends Msg> Class<T> getClass(byte serializerType, Class<T> msgInterface, Supplier<Object[]> supplier) throws IllegalStateException {
        AssertUtil.notNull(msgInterface, "msg interface");
        byte classKey = getClassKey(msgInterface);
        Class<?> realClass = getClass(serializerType, classKey);
        if (realClass == null){
            synchronized (this){
                realClass = getClass(serializerType, classKey);
                if (realClass == null){
                    realClass = generateClass(serializerType, classKey, msgInterface, supplier);
                }
            }
        }
        AssertUtil.notNull(realClass, "generate class");
        return (Class<T>) realClass;
    }
    
    private Class<?> generateClass(byte serializerType, byte classKey, Class<?> msgInterface, Supplier<Object[]> supplier){
        SerializedClassGenerator classGenerator = getClassGenerator(serializerType);
        try {
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(msgInterface);
            MsgGen msgGen = classGenerator.generate(msgInterface, methods, supplier == null ? null : supplier.get());
            Class<?> realClass = msgGen.getMsgClass();
            registerClass(serializerType, classKey, realClass);
            MsgFactory msgFactory = ReflectionUtils.accessibleConstructor(msgGen.getMsgFactory()).newInstance();
            registerMsgFactory(serializerType, classKey, msgFactory);
            return realClass;
        } catch (Exception e) {
            throw new RuntimeException("[" + msgInterface.getName() + "]", e);
        }
    }

    @Override
    public MsgFactory getMsgFactory(byte serializerType, byte classKey) {
        return this.msgFactoryMap.get(joinClassKey(serializerType, classKey));
    }

    @Override
    public <T extends Msg> T newObject(byte serializerType, byte classKey) {
        MsgFactory msgFactory = getMsgFactory(serializerType, classKey);
        AssertUtil.notNull(msgFactory, "msgFactory");
        Object instance;
        try {
            instance = msgFactory.create();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (T) instance;
    }

    @Override
    public <T extends Msg> T newObject(byte serializerType, Class<T> msgInterface, Supplier<Object[]> supplier) {
        Byte classKey = this.classKeyMap.get(msgInterface);
        MsgFactory msgFactory = getMsgFactory(serializerType, classKey);
        AssertUtil.notNull(msgFactory, "msgFactory");
        Object instance;
        try {
            instance = msgFactory.create();
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
