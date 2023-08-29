package com.feige.api.serialize;


import java.lang.reflect.Method;
import java.util.List;

public interface SerializedClassGenerator {
    

    MsgGen generate(Class<?> msgInterface, Method[] methods, Object... args);
    
    byte getSerializerType();
    
    List<ClassGen> getClassGen();
    
    
}
