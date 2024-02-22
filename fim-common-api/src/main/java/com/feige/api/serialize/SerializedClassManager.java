package com.feige.api.serialize;

import com.feige.api.msg.Msg;
import com.feige.api.msg.MsgFactory;

import java.util.function.Supplier;

public interface SerializedClassManager {
    void register(Serializer serializer);
    
    void registerClassGenerator(SerializedClassGenerator serializedClassGenerator);
    
    void registerClass(byte serializerType, byte classKey, Class<?> clazz);
    
    void registerMsgFactory(byte serializerType, byte classKey, MsgFactory msgFactory);
    
    Serializer unregister(byte serializerType);
    
    SerializedClassGenerator unregisterClassGenerator(byte serializerType);
    
    Class<?> unregisterClass(byte serializerType, byte classKey);
    
    MsgFactory unregisterMsgFactory(byte serializerType, byte classKey);
    
    Serializer getSerializer(byte serializerType);

    SerializedClassGenerator getClassGenerator(byte serializerType);
    
    byte getClassKey(Class<?> msgInterface);
    
    Class<?> getClass(byte serializerType, byte classKey)  throws IllegalStateException;

    default <T extends Msg> Class<T> getClass(byte serializerType, Class<T> msgInterface) throws IllegalStateException {
        return getClass(serializerType, msgInterface, null);
    }
    
    <T extends Msg> Class<T> getClass(byte serializerType, Class<T> msgInterface, Supplier<Object[]> supplier)  throws IllegalStateException;
    
    MsgFactory getMsgFactory(byte serializerType, byte classKey);

    <T extends Msg> T newObject(byte serializerType, byte classKey) throws IllegalStateException;
    
    default <T extends Msg> T newObject(byte serializerType, Class<T> msgInterface) throws IllegalStateException {
        return newObject(serializerType, msgInterface, null);
    }
    <T extends Msg> T newObject(byte serializerType, Class<T> msgInterface, Supplier<Object[]> supplier) throws IllegalStateException;
    
    Object getDeserializedObject(byte serializerType, byte classKey, byte[] bytes);

    default <T extends Msg> T getDeserializedObject(byte serializerType, byte classKey, byte[] bytes, Class<T> msgClass) {
        Object serializedObject = getDeserializedObject(serializerType, classKey, bytes);
        return msgClass.cast(serializedObject);
    }

    byte[] getSerializedObject(byte serializerType, Msg msg);
    
}
