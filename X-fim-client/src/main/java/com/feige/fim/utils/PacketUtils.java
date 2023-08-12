package com.feige.fim.utils;

import com.feige.api.constant.Command;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.msg.Msg;
import com.feige.api.serialize.SerializedClassManager;
import com.feige.fim.config.ClientConfig;
import com.feige.fim.protocol.Packet;
import com.feige.api.annotation.MsgComp;
import com.feige.framework.utils.AppContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketUtils {
    
    private static final Map<Class<?>, Byte> classKeyMap = new ConcurrentHashMap<>(32);
    private static final AtomicInteger cnt = new AtomicInteger();
    private static SerializedClassManager serializedClassManager;
    public static Packet createPacket(Command cmd, Class<?> msgInterface){
        Byte classKey = classKeyMap.computeIfAbsent(msgInterface, PacketUtils::getClassKey);
        Packet packet = Packet.create(cmd);
        packet.setSerializerType(ClientConfig.getSerializerType());
        packet.setClassKey(classKey);
        packet.setSequenceNum(cnt.incrementAndGet());
        return packet;
    }

    public static <T extends Msg>  Pair<Packet, T> createPacketPair(Command cmd, Class<T> msgInterface){
        byte serializerType = ClientConfig.getSerializerType();
        T t =  getSerializedClassManager().newObject(serializerType, msgInterface);
        Byte classKey = classKeyMap.computeIfAbsent(msgInterface, PacketUtils::getClassKey);
        Packet packet = Packet.create(cmd);
        packet.setSerializerType(serializerType);
        packet.setClassKey(classKey);
        packet.setSequenceNum(cnt.incrementAndGet());
        return Pair.of(packet, t);
    }
    
    public static byte[] getSerializedObject(Msg msg){
        return getSerializedClassManager().getSerializedObject(ClientConfig.getSerializerType(), msg);
    }
    
    public static SerializedClassManager getSerializedClassManager(){
        if (serializedClassManager == null){
            serializedClassManager = AppContext.get(SerializedClassManager.class);
        }
        return serializedClassManager;
    }
    
    public static byte getClassKey(Class<?> msgClass) {
        MsgComp msgComp = msgClass.getAnnotation(MsgComp.class);
        if (msgComp != null){
            return msgComp.classKey();
        }
        throw new RuntimeException("not found class key " + msgClass.getName());
    }
}
