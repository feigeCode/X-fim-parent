package com.feige.fim.codec;

import com.feige.api.codec.Codec;
import com.feige.api.codec.Command;
import com.feige.api.codec.DecoderException;
import com.feige.api.codec.EncoderException;
import com.feige.api.codec.IByteBuf;
import com.feige.api.codec.VersionException;
import com.feige.api.constant.Const;
import com.feige.api.session.ISession;

public class TransportCodec implements Codec {

    @Override
    public IByteBuf encode(ISession session, IByteBuf byteBuf, Object obj)  throws EncoderException {
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
    public Object decode(ISession session, IByteBuf byteBuf)  throws DecoderException {
        byteBuf.markReaderIndex();
        if (byteBuf.readable()) {
            if (byteBuf.readByte() == Const.HEARTBEAT) {
                return Transport.create(Command.HEARTBEAT);
            } else {
                byteBuf.resetReaderIndex();
                checkVersion(byteBuf);

            }

        }

        return null;
    }


    private void checkVersion(IByteBuf byteBuf){
        if (byteBuf.readByte() == Const.VERSION) {
            throw new VersionException("The protocol version does not match");
        }
    }

    private void checkChecksum(IByteBuf byteBuf){
        short cs = byteBuf.readShort();
        byte[] data = byteBuf.array();
        CheckSumUtils.check(data, cs);
    }

    private boolean checkLength(IByteBuf byteBuf){
        int len = byteBuf.readInt();
        return byteBuf.readableBytes() == len;
    }

    @Override
    public String getKey() {
        return "transport";
    }

    @Override
    public boolean isPrimary() {
        return true;
    }
}
