package com.feige.grpc;

import com.feige.api.constant.Command;
import com.feige.fim.protocol.Packet;
import com.feige.utils.convert.ObjectConvert;
import com.google.protobuf.ByteString;
import lombok.Getter;

public class PacketAndMessageConverter implements ObjectConvert<Packet, Message> {
    
    @Getter
    private static final PacketAndMessageConverter instance = new PacketAndMessageConverter();

    @Override
    public Message convertT(Packet packet) {
        return Message.newBuilder()
                .setCmd(packet.getCmd())
                .setVersion(packet.getVersion())
                .setFeats(packet.getFeats())
                .setCs(packet.getCs())
                .setSeqId(packet.getSeqId())
                .setRealType(packet.getRealType())
                .setSerializer(packet.getSerializer())
                .setData(ByteString.copyFrom(packet.getData()))
                .build();
    }
    
    @Override
    public Packet convertR(Message message) {
        int version = message.getVersion();
        int cmd = message.getCmd();
        int feats = message.getFeats();
        int seqId = message.getSeqId();
        int cs = message.getCs();
        int realType = message.getRealType();
        int serializer = message.getSerializer();
        Packet packet = Packet.create(Command.valueOf((byte) cmd));
        packet.setVersion((byte)version);
        packet.setFeats((byte)feats);
        packet.setSeqId((byte)seqId);
        packet.setCs((short) cs);
        packet.setRealType((byte)realType);
        packet.setSerializer((byte)serializer);
        return packet;
    }
    
}
