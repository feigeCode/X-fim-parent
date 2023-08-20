package com.feige.api.serialize;


import java.lang.reflect.Method;

public interface SerializedClassGenerator {
    

    MsgGen generate(Class<?> msgInterface, Method[] methods, Object... args);
    
    byte getSerializerType();
    
}
