package com.feige.fim.utils;

import com.feige.api.constant.Command;
import com.feige.api.constant.ProtocolConst;
import com.feige.fim.protocol.Packet;
import com.feige.framework.annotation.MsgComp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketUtils {
    
    private static final Map<Class<?>, Byte> classKeyMap = new ConcurrentHashMap<>(32);
    private static final AtomicInteger cnt = new AtomicInteger();
    public static Packet createPacket(Command cmd, Class<?> msgClass){
        Byte classKey = classKeyMap.computeIfAbsent(msgClass, PacketUtils::getClassKey);
        Packet packet = Packet.create(cmd);
        packet.setSerializerType(ProtocolConst.PROTOCOL_BUFFER);
        packet.setClassKey(classKey);
        packet.setSequenceNum(cnt.incrementAndGet());
        return packet;
    }
    
    
    public static byte getClassKey(Class<?> msgClass) {
        MsgComp msgComp = msgClass.getAnnotation(MsgComp.class);
        if (msgComp != null){
            return msgComp.classKey();
        }
        throw new RuntimeException("not found class key " + msgClass.getName());
    }
}
