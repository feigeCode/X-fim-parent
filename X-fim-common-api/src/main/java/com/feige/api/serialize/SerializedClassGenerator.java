package com.feige.api.serialize;

import com.feige.api.msg.MsgFactory;
import com.feige.fim.utils.Pair;
import com.feige.fim.utils.ReflectionUtils;

import java.lang.reflect.Method;

public interface SerializedClassGenerator {
    
    
    default Pair<Class<?> , Class<MsgFactory>> generate(Class<?> msgInterface, Object... args) {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(msgInterface);
        return generate(msgInterface, methods, args);
    }

    Pair<Class<?> , Class<MsgFactory>> generate(Class<?> msgInterface, Method[] methods, Object... args);
    
    byte getSerializerType();
    
}
