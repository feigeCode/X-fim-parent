package com.feige.fim.codec;

import com.feige.fim.protocol.Command;
import com.feige.fim.protocol.Packet;
import com.feige.fim.protocol.ProtocolConst;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

public abstract class AbstractNettyCodec implements Codec {
    
    private final int maxPacketSize;
    private final byte heartbeat;
    private final byte version;
    private final int headerLength;
    private ICheckSum checkSum;

    public AbstractNettyCodec(int maxPacketSize, byte heartbeat, byte version, int headerLength, String checkSumKey) {
        this.maxPacketSize = maxPacketSize;
        this.heartbeat = heartbeat;
        this.version = version;
        this.headerLength = headerLength;
        if (checkSumKey != null && checkSumKey.length() > 0){
            this.checkSum = getCheckSum(checkSumKey);
        }
    }

    

    @Override
    public void encode(Object p, Object b) throws EncoderException {
        Packet packet = (Packet) p;
        ByteBuf byteBuf = (ByteBuf) b;
        if (packet.getCmd() == Command.HEARTBEAT.getCmd()){
            byteBuf.writeByte(getHeartbeat());
        }else {
            int dataLength = packet.getDataLength();
            byteBuf.writeByte(getVersion());
            byteBuf.writeInt(dataLength);
            byteBuf.writeByte(packet.getCmd());
            byteBuf.writeInt(packet.getSequenceNum());
            byteBuf.writeByte(packet.getFeatures());
            byteBuf.writeShort(packet.getCs());
            byteBuf.writeByte(packet.getClassKey());
            if (dataLength > 0){
                byteBuf.writeBytes(packet.getData());
            }
        }
    }

    @Override
    public Object decode(Object b) throws DecoderException {
        ByteBuf byteBuf = (ByteBuf) b;
        byteBuf.markReaderIndex();
        if (byteBuf.isReadable()) {
            if (byteBuf.readByte() == ProtocolConst.HB_PACKET_BYTE) {
                Packet packet = Packet.create(Command.HEARTBEAT);
                packet.setVersion(getVersion());
                return packet;
            } else {
                byteBuf.resetReaderIndex();
                byte version = checkVersion(byteBuf);
                int bodyLength = checkLength(byteBuf);
                if (bodyLength != -1) {
                    byte cmd = byteBuf.readByte();
                    int sequenceNum = byteBuf.readInt();
                    byte features = byteBuf.readByte();
                    short checksum = checkChecksum(byteBuf);
                    byte[] data = new byte[bodyLength];
                    byteBuf.readBytes(data);
                    Packet packet = Packet.create(Command.valueOf(cmd));
                    packet.setSequenceNum(sequenceNum);
                    packet.setVersion(version);
                    packet.setFeatures(features);
                    packet.setCs(checksum);
                    packet.setData(data);
                    return packet;
                }else {
                    byteBuf.resetReaderIndex();
                }
            }
        }
        return null;
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
            getCheckSum().check(data, cs);
        }
        return cs;
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

    public abstract void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception;
    
    public abstract void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception ;

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
        return checkSum;
    }
    
    protected boolean isNeedCheckSum(){
        return checkSum != null;
    }
    
    protected ICheckSum getCheckSum(String checkSumKey){
        ICheckSum checkSum = null;
        if (ProtocolConst.CHECK_SUM_DEFAULT.equals(checkSumKey)) {
            checkSum = new DefaultCheckSum();
        }else if (ProtocolConst.CHECK_SUM_CRC32.equals(checkSumKey)){
            checkSum = new CRC32CheckSum();
        }
        return checkSum;
    }
}
