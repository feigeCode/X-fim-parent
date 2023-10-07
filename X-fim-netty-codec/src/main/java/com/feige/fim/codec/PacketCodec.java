package com.feige.fim.codec;

import com.feige.api.codec.PacketInterceptor;
import com.feige.api.codec.Codec;
import com.feige.api.codec.DecoderException;
import com.feige.api.codec.EncoderException;
import com.feige.api.codec.ICheckSum;
import com.feige.api.codec.VersionException;
import com.feige.api.session.Session;
import com.feige.api.constant.Command;
import com.feige.fim.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.TooLongFrameException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PacketCodec implements Codec {
    
    private final int maxPacketSize;
    private final byte heartbeat;
    private final byte version;
    private final int headerLength;
    private final ICheckSum checkSum;
    private final List<PacketInterceptor> packetInterceptors;
    private final List<PacketInterceptor> reversePacketInterceptors;

    public PacketCodec(int maxPacketSize, byte heartbeat, byte version, int headerLength, ICheckSum checkSum, List<PacketInterceptor> packetInterceptors) {
        this.maxPacketSize = maxPacketSize;
        this.heartbeat = heartbeat;
        this.version = version;
        this.headerLength = headerLength;
        this.checkSum = checkSum;
        this.packetInterceptors = packetInterceptors;
        this.reversePacketInterceptors = new ArrayList<>(packetInterceptors);
        this.reversePacketInterceptors.sort(Comparator.comparingInt(PacketInterceptor::order).reversed());
    }

    

    @Override
    public void encode(Session session ,Object msg, Object b) throws EncoderException {
        if (!(msg instanceof Packet)){
            return;
        }
        Packet packet = (Packet) msg; 
        ByteBuf byteBuf = (ByteBuf) b;
        if (packet.getCmd() == Command.HEARTBEAT.getCmd()){
            byteBuf.writeByte(getHeartbeat());
        }else {
            // 包拦截器，对包做一些处理
            getPacketInterceptors()
                    .forEach(packetInterceptor -> packetInterceptor.writePacket(session, packet));
            int dataLength = packet.getDataLength();
            byteBuf.writeByte(getVersion());
            byteBuf.writeInt(dataLength);
            byteBuf.writeByte(packet.getCmd());
            byteBuf.writeInt(packet.getSequenceNum());
            byteBuf.writeByte(packet.getFeatures());
            byteBuf.writeShort(packet.getCs());
            byteBuf.writeByte(packet.getSerializerType());
            byteBuf.writeByte(packet.getClassKey());
            if (dataLength > 0){
                byteBuf.writeBytes(packet.getData());
            }
        }
    }

    @Override
    public void decode(Session session, Object b, List<Object> out) throws DecoderException {
        if (!(b instanceof ByteBuf)){
            return;
        }
        ByteBuf byteBuf = (ByteBuf) b;
        byteBuf.markReaderIndex();
        if (byteBuf.isReadable()) {
            if (byteBuf.readByte() == getHeartbeat()) {
                Packet packet = Packet.create(Command.HEARTBEAT);
                packet.setVersion(getVersion());
                out.add(packet);
            } else {
                byteBuf.resetReaderIndex();
                byte version = checkVersion(byteBuf);
                int bodyLength = checkLength(byteBuf);
                if (bodyLength != -1) {
                    byte cmd = byteBuf.readByte();
                    int sequenceNum = byteBuf.readInt();
                    byte features = byteBuf.readByte();
                    short checksum = checkChecksum(byteBuf);
                    byte serializerType = byteBuf.readByte();
                    byte classKey = byteBuf.readByte();
                    byte[] data = new byte[bodyLength];
                    byteBuf.readBytes(data);
                    Packet packet = Packet.create(Command.valueOf(cmd));
                    packet.setSequenceNum(sequenceNum);
                    packet.setVersion(version);
                    packet.setFeatures(features);
                    packet.setCs(checksum);
                    packet.setSerializerType(serializerType);
                    packet.setClassKey(classKey);
                    packet.setData(data);
                    // 包拦截器，对包做一些处理
                    reversePacketInterceptors
                            .forEach(packetInterceptor -> packetInterceptor.readPacket(session, packet));
                    out.add(packet);
                }else {
                    byteBuf.resetReaderIndex();
                }
            }
        }
    }
    
    

    private byte checkVersion(ByteBuf byteBuf) {
        byte version = byteBuf.readByte();
        if (version != getVersion()) {
            throw new VersionException("The protocol version does not match");
        }
        return version;
    }

    private short checkChecksum(ByteBuf byteBuf){
        short cs = byteBuf.readShort();
        if (isNeedCheckSum()){
            byte[] data = byteBuf.array();
            checkSum.check(data, cs);
        }
        return cs;
    }

    protected boolean isNeedCheckSum(){
        return checkSum != null;
    }

    private int checkLength(ByteBuf byteBuf) {
        int bodyLength = byteBuf.readInt();
        if (bodyLength < 0){
            throw new DecoderException("negative length: " + bodyLength);
        }
        if (bodyLength > getMaxPacketSize()) {
            throw new TooLongFrameException("packet body length over limit:" + bodyLength);
        }
        int readableBytes = byteBuf.readableBytes();
        return bodyLength + (getHeaderLength() - 4 - 1) <= readableBytes ? bodyLength : -1;
    }

    @Override
    public byte getVersion() {
        return version;
    }

    @Override
    public byte getHeartbeat() {
        return heartbeat;
    }

    @Override
    public int getMaxPacketSize() {
        return maxPacketSize;
    }

    @Override
    public int getHeaderLength() {
        return headerLength;
    }

    @Override
    public ICheckSum getCheckSum() {
        return this.checkSum;
    }

    @Override
    public List<PacketInterceptor> getPacketInterceptors() {
        return packetInterceptors;
    }
}
