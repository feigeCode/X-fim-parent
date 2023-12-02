package com.feige.grpc;

import com.feige.api.constant.Command;
import com.feige.fim.protocol.Packet;
import com.feige.utils.convert.ObjectConvert;
import com.google.protobuf.ByteString;

public class PacketAndMessageConverter implements ObjectConvert<Packet, Message> {
    
    private static final PacketAndMessageConverter instance = new PacketAndMessageConverter();

    public static PacketAndMessageConverter getInstance() {
        return instance;
    }
    
    @Override
    public Message convertT(Packet packet) {
        return Message.newBuilder()
                .setCmd(packet.getCmd())
                .setVersion(packet.getVersion())
                .setFeatures(packet.getFeatures())
                .setCs(packet.getCs())
                .setSequenceNum(packet.getSequenceNum())
                .setClassKey(packet.getClassKey())
                .setSerializerType(packet.getSerializerType())
                .setData(ByteString.copyFrom(packet.getData()))
                .build();
    }
    
    @Override
    public Packet convertR(Message message) {
        int version = message.getVersion();
        int cmd = message.getCmd();
        int features = message.getFeatures();
        int sequenceNum = message.getSequenceNum();
        int cs = message.getCs();
        int classKey = message.getClassKey();
        int serializerType = message.getSerializerType();
        Packet packet = Packet.create(Command.valueOf((byte) cmd));
        packet.setVersion((byte)version);
        packet.setFeatures((byte)features);
        packet.setSequenceNum((byte)sequenceNum);
        packet.setCs((short) cs);
        packet.setClassKey((byte)classKey);
        packet.setSerializerType((byte)serializerType);
        return packet;
    }
    
}
