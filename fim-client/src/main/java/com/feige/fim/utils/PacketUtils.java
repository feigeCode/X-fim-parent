package com.feige.fim.utils;

import com.feige.api.constant.Command;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.msg.BindClientReq;
import com.feige.api.msg.Msg;
import com.feige.api.serialize.SerializedClassManager;
import com.feige.fim.config.ClientConfig;
import com.feige.fim.protocol.Packet;
import com.feige.framework.utils.AppContext;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class PacketUtils {
    
    private static final AtomicInteger cnt = new AtomicInteger();
    private static SerializedClassManager serializedClassManager;
    
    public static Packet createPacket(Command cmd, Class<?> msgInterface){
        byte classKey = getSerializedClassManager().getClassKey(msgInterface);
        Packet packet = Packet.create(cmd);
        packet.setSerializer(ClientConfig.getSerializerType());
        packet.setRealType(classKey);
        packet.setSeqId(cnt.incrementAndGet());
        return packet;
    }

    public static <T extends Msg> T newObject(Class<T> msgInterface){
        byte serializerType = ClientConfig.getSerializerType();
        return getSerializedClassManager().newObject(serializerType, msgInterface);
    }
    
    public static byte[] getSerializedObject(Msg msg){
        byte serializerType = ClientConfig.getSerializerType();
        return getSerializedClassManager().getSerializedObject(serializerType, msg);
    }
    
    public static SerializedClassManager getSerializedClassManager(){
        if (serializedClassManager == null){
            serializedClassManager = AppContext.get(SerializedClassManager.class);
        }
        return serializedClassManager;
    }
    
    
    public static Packet createBindClientPacket(){
        Packet packet = createPacket(Command.BIND, BindClientReq.TYPE);
        BindClientReq bindClientReq = newObject(BindClientReq.TYPE)
                .setSessionId(ClientConfig.getSessionId())
                .setTags(ClientConfig.getTags());
        packet.setData(getSerializedObject(bindClientReq));
        return packet;
    }

    public static  <T extends Msg> Packet buildPacket(Command command, ProtocolConst.SerializedClass serializedClass, Consumer<T> consumer){
        byte serializerType = ClientConfig.getSerializerType();
        byte classKey = serializedClass.getClassKey();
        Packet packet = Packet.create(command);
        packet.setSerializer(serializerType);
        packet.setRealType(classKey);
        packet.setSeqId(cnt.incrementAndGet());
        T msg = serializedClassManager.newObject(serializerType, classKey);
        consumer.accept(msg);
        packet.setData(serializedClassManager.getSerializedObject(serializerType, msg));
        return packet;
    }
    
}
