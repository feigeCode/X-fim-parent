package com.feige.api.serialize;


import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSerializedClassGenerator implements SerializedClassGenerator {

    public static final Set<String> CUSTOM_METHOD = new HashSet<>();

    static {
        CUSTOM_METHOD.add("serialize");
        CUSTOM_METHOD.add("deserialize");
    }
    

    protected String getFieldName(Class<?> returnType, String methodName){
        String fieldName;
        if (Boolean.class.equals(returnType) || Boolean.TYPE.equals(returnType)){
            if (methodName.startsWith("is")){
                fieldName = methodName.replaceFirst("is", "");
            }else {
                fieldName = methodName.replaceFirst("get", "");
            }
        }else {
            fieldName = methodName.replaceFirst("get", "");
        }
        return fieldName;
    }
}
