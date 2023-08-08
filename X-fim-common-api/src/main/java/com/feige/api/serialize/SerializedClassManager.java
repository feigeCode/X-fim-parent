package com.feige.api.serialize;

import com.feige.api.msg.Msg;

public interface SerializedClassManager {
    void register(Serializer serializer);
    
    void registerClass(byte serializerType, byte classKey, Class<?> clazz);
    
    Serializer unregister(byte serializerType);
    
    Class<?> unregisterClass(byte serializerType, byte classKey);
    
    Serializer getSerializer(byte serializerType);
    
    Class<?> getClass(byte serializerType, byte classKey);
    
    <T> T newObject(byte serializerType, byte classKey);
    
    Object getDeserializedObject(byte serializerType, byte classKey, byte[] bytes);

    default <T extends Msg> T getDeserializedObject(byte serializerType, byte classKey, byte[] bytes, Class<T> msgClass) {
        Object serializedObject = getDeserializedObject(serializerType, classKey, bytes);
        return msgClass.cast(serializedObject);
    }

    byte[] getSerializedObject(byte serializerType, Msg msg);
    
}
