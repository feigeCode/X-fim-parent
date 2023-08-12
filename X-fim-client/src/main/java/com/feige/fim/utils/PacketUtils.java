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
    
    private static final AtomicInteger cnt = new AtomicInteger();
    private static SerializedClassManager serializedClassManager;
    
    public static Packet createPacket(Command cmd, Class<?> msgInterface){
        byte classKey = getSerializedClassManager().getClassKey(msgInterface);
        Packet packet = Packet.create(cmd);
        packet.setSerializerType(ClientConfig.getSerializerType());
        packet.setClassKey(classKey);
        packet.setSequenceNum(cnt.incrementAndGet());
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
    
}
