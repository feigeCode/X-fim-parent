package com.feige.api.handler;

import com.feige.framework.annotation.InitMethod;
import com.feige.framework.annotation.Inject;
import com.feige.api.annotation.MsgComp;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.serialize.SerializedClassManager;
import com.feige.fim.utils.ClassGenerator;
import com.feige.fim.utils.ReflectionUtils;
import com.feige.fim.utils.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * @author feige<br />
 * @ClassName: AbstractMsgHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/25 21:50<br/>
 */
public abstract class AbstractMsgHandler<T> implements MsgHandler<T> {

    public static final Set<String> CUSTOM_METHOD = new HashSet<>();
    
    static {
        CUSTOM_METHOD.add("serialize");
        CUSTOM_METHOD.add("deserialize");
    }
    
    @Inject
    protected SerializedClassManager serializedClassManager;
    
    @InitMethod
    public void initialize(){
        Class<?> type = getMsgInterface();
        if (type == null || !type.isAnnotationPresent(MsgComp.class)){
            // TODO: LOGGER
            return;
        }
        MsgComp msgComp = type.getAnnotation(MsgComp.class);
        byte classKey = msgComp.classKey();
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(type);
        serializedClassManager.registerClass(ProtocolConst.JSON, classKey, genBasicClass(type, methods));
        if (this.getProtoClass() != null){
            serializedClassManager.registerClass(ProtocolConst.PROTOCOL_BUFFER, classKey, wrapperProtoClass(type, methods));
        }
    }
    
    public abstract Class<?> getMsgInterface();
    
    public abstract Class<?> getProtoClass();
    
    protected Class<?> genBasicClass(Class<?> type, Method[] methods){
        try(ClassGenerator classGenerator = new ClassGenerator()) {
            classGenerator.addInterface(type.getName());
            classGenerator.setClassName(type.getSimpleName());
            for (Method method : methods) {
                String name = method.getName();
                if (CUSTOM_METHOD.contains(name)) {
                    continue;
                }
                Class<?> returnType = method.getReturnType();
                String fieldName = getFieldName(returnType, name);
                classGenerator.addField(StringUtils.uncapitalize(fieldName), Modifier.PRIVATE, returnType, true);
            }
            classGenerator.addMethod("public byte[] serialize(){\n" +
                    "return null;\n" + 
                    "}");
            classGenerator.addMethod("public void deserialize(byte[] bytes){}");
            return classGenerator.generate(type);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    
    protected Class<?>  wrapperProtoClass(Class<?> type, Method[] methods){
        try(ClassGenerator classGenerator = new ClassGenerator()) {
            classGenerator.addInterface(type.getName());
            classGenerator.setClassName(type.getSimpleName());
            classGenerator.addField("protoTarget", Modifier.PRIVATE, getProtoClass(), true);
            for (Method method : methods) {
                String name = method.getName();
                if (CUSTOM_METHOD.contains(name)) {
                    continue;
                }
                Class<?> returnType = method.getReturnType();
                classGenerator.addMethod(name, Modifier.PUBLIC, returnType, null, "\nreturn this.protoTarget." + name + "();\n");
            }
            classGenerator.addMethod("public byte[] serialize(){\n" +
                    "return this.protoTarget.toByteArray();\n" +
                    "}");
            classGenerator.addMethod("public void deserialize(byte[] bytes){\n" +
                    "this.protoTarget = " + getProtoClass().getName() + ".parseFrom(bytes);\n" +
                    "}");
            return classGenerator.generate(type);
        } catch (Exception e){
            throw new RuntimeException(e);   
        }
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
