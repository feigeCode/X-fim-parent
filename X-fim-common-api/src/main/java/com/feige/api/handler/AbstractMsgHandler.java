package com.feige.api.handler;

import com.feige.fim.utils.Pair;
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
        Pair<Class<?>, Class<?>> protoClassPair = getProtoClass();
        genClass(type, protoClassPair);
    }
    
    public abstract Class<?> getMsgInterface();
    
    public abstract Pair<Class<?>, Class<?>> getProtoClass();
    
    
    protected void genClass(Class<?> type, Pair<Class<?>, Class<?>> protoClassPair){
        if (type == null || !type.isAnnotationPresent(MsgComp.class)){
            // TODO: LOGGER
            return;
        }
        MsgComp msgComp = type.getAnnotation(MsgComp.class);
        byte classKey = msgComp.classKey();
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(type);
        serializedClassManager.registerClass(ProtocolConst.JSON, classKey, genBasicClass(type, methods));
        if (protoClassPair != null && protoClassPair.getK() != null && protoClassPair.getV() != null){
            serializedClassManager.registerClass(ProtocolConst.PROTOCOL_BUFFER, classKey, wrapperProtoClass(type, methods, protoClassPair));
        }
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
    
    protected Class<?>  wrapperProtoClass(Class<?> type, Method[] methods, Pair<Class<?>, Class<?>> protoClassPair ){
        try(ClassGenerator classGenerator = new ClassGenerator()) {
            classGenerator.setClassName(type.getSimpleName());
            classGenerator.addInterface(type.getName())
                    .addField("protoTarget", Modifier.PRIVATE, protoClassPair.getK(), true)
                    .addField("builder", Modifier.PRIVATE, protoClassPair.getV(), false)
                    .addMethod(
                            "private " + protoClassPair.getV().getName() + " getBuilder(){\n" +
                                "if (this.builder == null){\n" +
                                "   this.builder = " + protoClassPair.getK().getName() + ".newBuilder();\n" +
                                "}\n" +
                                " return this.builder;\n" +
                            " }"
                    );
            for (Method method : methods) {
                String name = method.getName();
                if (CUSTOM_METHOD.contains(name) || name.startsWith("set")) {
                    continue;
                }
                Class<?> returnType = method.getReturnType();
                String setterName = "set" + StringUtils.capitalize(getFieldName(returnType, name));
                classGenerator.addMethod(name, Modifier.PUBLIC, returnType, null, "\nreturn this.protoTarget." + name + "();\n")
                        .addMethod(setterName, Modifier.PUBLIC, type, new Class[]{returnType}, "\ngetBuilder()." + setterName + "(arg0);\nreturn this;\n");
            }
            classGenerator.addMethod(
                    "public byte[] serialize(){\n" +
                        "if (this.protoTarget == null && this.builder != null){\n" +
                        "    this.protoTarget = this.builder.build();\n" + 
                        "}\n" +
                        "if (this.protoTarget != null){\n" +
                        "     return this.protoTarget.toByteArray();\n" +
                        "}\n" +
                        "return new byte[0];\n" +
                    "}\n"
            );
            classGenerator.addMethod(
                    "public void deserialize(byte[] bytes){\n" +
                        "try{\n" +
                            "this.protoTarget = " + protoClassPair.getK().getName() + ".parseFrom(bytes);\n" +
                        "}catch(" + Exception.class.getName() + " e){\n" +
                            "throw new " + RuntimeException.class.getName() + "(e);\n" +
                        "}\n" +
                    "}\n"
            );
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
