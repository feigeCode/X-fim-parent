package com.feige.fim.serialize;

import com.feige.api.constant.ProtocolConst;
import com.feige.api.msg.BindClientReq;
import com.feige.api.msg.ErrorResp;
import com.feige.api.msg.FastConnectReq;
import com.feige.api.msg.FastConnectResp;
import com.feige.api.msg.HandshakeReq;
import com.feige.api.msg.HandshakeResp;
import com.feige.api.msg.MsgFactory;
import com.feige.api.serialize.ClassGen;
import com.feige.api.serialize.MsgGen;
import com.feige.api.serialize.SerializedClassGenerator;
import com.feige.fim.msg.proto.BindClientReqProto;
import com.feige.fim.msg.proto.ErrorRespProto;
import com.feige.fim.msg.proto.FastConnectReqProto;
import com.feige.fim.msg.proto.FastConnectRespProto;
import com.feige.fim.msg.proto.HandshakeReqProto;
import com.feige.fim.msg.proto.HandshakeRespProto;
import com.feige.utils.javassist.ClassGenerator;
import com.feige.utils.common.Pair;
import com.feige.utils.common.StringUtils;
import com.feige.utils.spi.annotation.SpiComp;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@SpiComp(interfaces = SerializedClassGenerator.class)
public class ProtoBufSerializedClassGenerator extends AbstractSerializedClassGenerator {

    @Override
    public MsgGen generate(Class<? > msgInterface, Method[] methods, Object... args) {
        if (args.length < 2){
            throw new IllegalArgumentException("args length < 2");
        }
        Class<?> protoClass = (Class<?>) args[0];
        Class<?> protoBuilderClass = (Class<?>) args[1];
        Pair<Class<?>, Class<?>> protoClassPair = Pair.of(protoClass, protoBuilderClass);
        return wrapperProtoClass(msgInterface, methods, protoClassPair);
        
    }


    protected MsgGen  wrapperProtoClass(Class<?> type, Method[] methods, Pair<Class<?>, Class<?>> protoClassPair ){
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
            Class<?> implClass = classGenerator.generate(type);
            Class<MsgFactory> msgFactoryClass = this.generateMsgFactory(type, implClass);
            return new MsgGen(implClass, msgFactoryClass);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte getSerializerType() {
        return ProtocolConst.PROTOCOL_BUFFER;
    }

    @Override
    public List<ClassGen> getClassGen() {
        List<ClassGen> classGens = new ArrayList<>();
        classGens.add(new ClassGen(HandshakeReq.TYPE, HandshakeReqProto.class, HandshakeReqProto.Builder.class));
        classGens.add(new ClassGen(HandshakeResp.TYPE, HandshakeRespProto.class, HandshakeRespProto.Builder.class));
        classGens.add(new ClassGen(FastConnectReq.TYPE, FastConnectReqProto.class, FastConnectReqProto.Builder.class));
        classGens.add(new ClassGen(FastConnectResp.TYPE, FastConnectRespProto.class, FastConnectRespProto.Builder.class));
        classGens.add(new ClassGen(BindClientReq.TYPE, BindClientReqProto.class, BindClientReqProto.Builder.class));
        classGens.add(new ClassGen(ErrorResp.TYPE, ErrorRespProto.class, ErrorRespProto.Builder.class));
        return classGens;
    }
}
