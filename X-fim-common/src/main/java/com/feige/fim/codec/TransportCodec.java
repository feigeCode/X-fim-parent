package com.feige.fim.codec;

import com.feige.api.codec.Codec;
import com.feige.api.codec.Command;
import com.feige.api.codec.DecoderException;
import com.feige.api.codec.EncoderException;
import com.feige.api.codec.IByteBuf;
import com.feige.api.codec.VersionException;
import com.feige.api.constant.Const;
import com.feige.api.session.Session;
import com.feige.fim.lg.Loggers;
import org.slf4j.Logger;

public class TransportCodec implements Codec {

    private static final Logger LOG = Loggers.CODEC;
    @Override
    public IByteBuf encode(Session session, IByteBuf byteBuf, Object obj)  throws EncoderException {
        if (obj instanceof Transport) {
            Transport packet = (Transport) obj;
            if (packet.getCmd() == Command.HEARTBEAT.getCmd()){
                byteBuf.writeByte(Const.HEARTBEAT);
            }else {
                byteBuf.writeByte(packet.getVersion());
                byteBuf.writeInt(packet.getBodyLength());
                byteBuf.writeLong(packet.getSrcId());
                byteBuf.writeLong(packet.getDestId());
                byteBuf.writeByte(packet.getCmd());
                byteBuf.writeByte(packet.getFeatures());
                byteBuf.writeByte(packet.getSerializeType());
                byteBuf.writeShort(packet.getCs());
                if (packet.getBodyLength() > 0){
                    byteBuf.writeBytes(packet.getBody());
                }
            }
        }
        return byteBuf;
    }


    @Override
    public Object decode(Session session, IByteBuf byteBuf)  throws DecoderException {
        byteBuf.markReaderIndex();
        if (byteBuf.readable()) {
            if (byteBuf.readByte() == Const.HEARTBEAT) {
                return Transport.create(Command.HEARTBEAT);
            } else {
                byteBuf.resetReaderIndex();
                byte version = checkVersion(byteBuf, session);
                int bodyLength = checkLength(byteBuf, session);
                if (bodyLength != -1) {
                    long srcId = byteBuf.readLong();
                    long destId = byteBuf.readLong();
                    byte cmd = byteBuf.readByte();
                    byte features = byteBuf.readByte();
                    byte serializeType = byteBuf.readByte();
                    short checksum = checkChecksum(byteBuf, session);
                    byte[] body = new byte[bodyLength];
                    byteBuf.readBytes(body);
                    Transport transport = Transport.create(Command.valueOf(cmd));
                    transport.setVersion(version);
                    transport.setSrcId(srcId);
                    transport.setDestId(destId);
                    transport.setFeatures(features);
                    transport.setSerializeType(serializeType);
                    transport.setCs(checksum);
                    transport.setBody(body);
                }else {
                    byteBuf.resetReaderIndex();
                }
            }

        }

        return null;
    }


    private byte checkVersion(IByteBuf byteBuf, Session session){
        byte version = byteBuf.readByte();
        if (version != Const.VERSION) {
            session.close();
            throw new VersionException("The protocol version does not match");
        }
        return version;
    }

    private short checkChecksum(IByteBuf byteBuf, Session session){
        short cs = byteBuf.readShort();
        byte[] data = byteBuf.array();
        CheckSumUtils.check(data, cs);
        return cs;
    }

    private int checkLength(IByteBuf byteBuf, Session session){
        int bodyLength = byteBuf.readInt();
        if (bodyLength < 0){
            LOG.error("sessionId : {}, negative length: {}",session.getId(),  bodyLength);
            session.close();
            return -1;
        }
        int readableBytes = byteBuf.readableBytes();
        return bodyLength + (Const.HEADER_LEN - 4 - 1) <= readableBytes ? bodyLength : -1;
    }

    @Override
    public String getKey() {
        return "transport";
    }
    
}
