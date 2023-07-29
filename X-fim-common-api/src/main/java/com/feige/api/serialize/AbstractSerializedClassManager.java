package com.feige.api.serialize;

import com.feige.api.msg.Msg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractSerializedClassManager implements SerializedClassManager {
    
    protected final Map<Byte, Serializer> serializerMap = new ConcurrentHashMap<>();
    protected final Map<String, Class<?>> classMap = new ConcurrentHashMap<>();

    @Override
    public void register(Serializer serializer) {
        serializerMap.put(serializer.getType(), serializer);
    }

    @Override
    public void registerClass(byte serializerType, byte classKey, Class<?> clazz) {
        classMap.put(joinClassKey(serializerType, classKey), clazz);
    }

    @Override
    public Serializer unregister(byte serializerType) {
        return serializerMap.remove(serializerType);
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
    public Class<?> getClass(byte serializerType, byte classKey) {
        return classMap.get(joinClassKey(serializerType, classKey));
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
