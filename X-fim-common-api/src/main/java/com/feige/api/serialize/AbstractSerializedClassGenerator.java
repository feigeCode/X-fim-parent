package com.feige.api.serialize;


import com.feige.api.msg.MsgFactory;
import com.feige.fim.utils.ClassGenerator;
import com.feige.fim.utils.ClassPoolUtils;
import javassist.ClassClassPath;
import javassist.ClassPool;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSerializedClassGenerator implements SerializedClassGenerator {

    public static final Set<String> CUSTOM_METHOD = new HashSet<>();

    static {
        CUSTOM_METHOD.add("serialize");
        CUSTOM_METHOD.add("deserialize");
    }

    public static final  Class<MsgFactory> msgFactoryClass = MsgFactory.class;

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
    
    
    protected Class<MsgFactory> generateMsgFactory(Class<?> msgInterface, Class<?> implClass) {
        try (ClassGenerator classGenerator = new ClassGenerator()){
            String implClassName = implClass.getName();
            classGenerator.setClassName(implClassName + msgFactoryClass.getSimpleName());
            classGenerator.addInterface(msgFactoryClass.getName())
                    .addMethod("create", Modifier.PUBLIC, Object.class, new Class[0], "\nreturn new " + implClassName + "();\n");
            return (Class<MsgFactory>) classGenerator.generate(msgFactoryClass);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        
    }
}
