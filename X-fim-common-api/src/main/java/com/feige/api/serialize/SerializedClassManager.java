package com.feige.api.serialize;

import com.feige.api.msg.Msg;

public interface SerializedClassManager<T extends Msg> {
    void register(Serializer serializer);
    
    void registerClass(byte serializerType, byte classKey, Class<T> clazz);
    
    Serializer unregister(byte serializerType);
    
    Class<T> unregisterClass(byte serializerType, byte classKey);
    
    Serializer getSerializer(byte serializerType);
    
    Class<T> getSerializedClass(byte serializerType, byte classKey);
    
    Object getSerializedObject(byte serializerType, byte classKey, byte[] bytes);

    default <T extends Msg> T getSerializedObject(byte serializerType, byte classKey, byte[] bytes, Class<T> msgClass) {
        Object serializedObject = getSerializedObject(serializerType, classKey, bytes);
        return msgClass.cast(serializedObject);
    }

    byte[] getDeserializedObject(byte serializerType, Msg msg);
    
}
