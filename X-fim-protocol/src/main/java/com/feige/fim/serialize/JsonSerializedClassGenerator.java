package com.feige.fim.serialize;

import com.feige.api.constant.ProtocolConst;
import com.feige.api.serialize.AbstractSerializedClassGenerator;
import com.feige.api.serialize.SerializedClassGenerator;
import com.feige.fim.utils.ClassGenerator;
import com.feige.fim.utils.StringUtils;
import com.feige.framework.annotation.SpiComp;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
@SpiComp(interfaces = SerializedClassGenerator.class)
public class JsonSerializedClassGenerator extends AbstractSerializedClassGenerator {

    
    @Override
    public Class<?> generate(Class<?> msgInterface, Method[] methods, Object... args) {
        return genBasicClass(msgInterface, methods);
        
        
    }

    protected Class<?> genBasicClass(Class<?> type, Method[] methods){
        try(ClassGenerator classGenerator = new ClassGenerator()) {
            classGenerator.addInterface(type.getName());
            classGenerator.setClassName(type.getSimpleName());
            for (Method method : methods) {
                String name = method.getName();
                if (CUSTOM_METHOD.contains(name) || name.startsWith("set")) {
                    continue;
                }
                Class<?> returnType = method.getReturnType();
                String fieldName = getFieldName(returnType, name);
                classGenerator.addField(StringUtils.uncapitalize(fieldName), Modifier.PRIVATE, returnType, true, type);
            }
            classGenerator.addMethod(
                    "public byte[] serialize(){\n" +
                            "return null;\n" +
                            "}\n"
            );
            classGenerator.addMethod(
                    "public void deserialize(byte[] bytes){}\n"
            );
            return classGenerator.generate(type);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte getSerializerType() {
        return ProtocolConst.JSON;
    }
}
