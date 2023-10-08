package com.feige.fim.serialize;

import com.feige.api.constant.ProtocolConst;
import com.feige.api.msg.Ack;
import com.feige.api.msg.BindClientReq;
import com.feige.api.msg.ChatMsgReq;
import com.feige.api.msg.ChatMsgResp;
import com.feige.api.msg.ErrorResp;
import com.feige.api.msg.FastConnectReq;
import com.feige.api.msg.SuccessResp;
import com.feige.api.msg.HandshakeReq;
import com.feige.api.msg.HandshakeResp;
import com.feige.api.msg.MsgFactory;
import com.feige.api.serialize.ClassGen;
import com.feige.api.serialize.MsgGen;
import com.feige.api.serialize.SerializedClassGenerator;
import com.feige.utils.javassist.ClassGenerator;
import com.feige.utils.common.StringUtils;
import com.feige.utils.spi.annotation.SpiComp;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@SpiComp(interfaces = SerializedClassGenerator.class)
public class JsonSerializedClassGenerator extends AbstractSerializedClassGenerator {

    
    
    @Override
    public MsgGen generate(Class<?> msgInterface, Method[] methods, Object... args) {
        return genBasicClass(msgInterface, methods);
        
        
    }

    protected MsgGen genBasicClass(Class<?> type, Method[] methods){
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
            Class<?> implClass = classGenerator.generate(type);
            Class<MsgFactory> msgFactoryClass = this.generateMsgFactory(type, implClass);
            return new MsgGen(implClass, msgFactoryClass);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte getSerializerType() {
        return ProtocolConst.JSON;
    }

    @Override
    public List<ClassGen> getClassGen() {
        List<ClassGen> classGens = new ArrayList<>();
        classGens.add(new ClassGen(HandshakeReq.TYPE));
        classGens.add(new ClassGen(HandshakeResp.TYPE));
        classGens.add(new ClassGen(FastConnectReq.TYPE));
        classGens.add(new ClassGen(SuccessResp.TYPE));
        classGens.add(new ClassGen(BindClientReq.TYPE));
        classGens.add(new ClassGen(ErrorResp.TYPE));
        classGens.add(new ClassGen(Ack.TYPE));
        classGens.add(new ClassGen(ChatMsgReq.TYPE));
        classGens.add(new ClassGen(ChatMsgResp.TYPE));
        return classGens;
    }
    
    
    
}
