package com.feige.api.serialize;

import com.feige.api.msg.Msg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractSerializedClassManager<T extends Msg> implements SerializedClassManager<T>{
    
    protected final Map<Byte, Serializer> serializerMap = new ConcurrentHashMap<>();
    protected final Map<String, Class<T>> classMap = new ConcurrentHashMap<>();

    @Override
    public void register(Serializer serializer) {
        serializerMap.put(serializer.getType(), serializer);
    }

    @Override
    public void registerClass(byte serializerType, byte classKey, Class<T> clazz) {
        classMap.put(joinClassKey(serializerType, classKey), clazz);
    }

    @Override
    public Serializer unregister(byte serializerType) {
        return serializerMap.remove(serializerType);
    }

    @Override
    public Class<T> unregisterClass(byte serializerType, byte classKey) {
        return classMap.remove(joinClassKey(serializerType, classKey));
    }

    @Override
    public Serializer getSerializer(byte serializerType) {
        return serializerMap.get(serializerType);
    }

    @Override
    public Class<T> getSerializedClass(byte serializerType, byte classKey) {
        return classMap.get(joinClassKey(serializerType, classKey));
    }

    @Override
    public Object getSerializedObject(byte serializerType, byte classKey, byte[] bytes) {
        Serializer serializer = getSerializer(serializerType);
        Class<T> serializedClass = getSerializedClass(serializerType, classKey);
        if (serializer != null && serializedClass != null){
            return serializer.deserialize(serializedClass, bytes);
        }
        return null;
    }

    @Override
    public byte[] getDeserializedObject(byte serializerType, Msg msg) {
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
