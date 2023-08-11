package com.feige.api.serialize;

import com.feige.fim.utils.ReflectionUtils;

import java.lang.reflect.Method;

public interface SerializedClassGenerator {
    
    
    default Class<?> generate(Class<?> msgInterface, Object... args) {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(msgInterface);
        return generate(msgInterface, methods, args);
    }
    
    Class<?> generate(Class<?> msgInterface, Method[] methods, Object... args);
    
    byte getSerializerType();
    
}
