package com.feige.fim.codec;

import com.feige.api.codec.Codec;
import com.feige.api.codec.DecoderException;
import com.feige.api.codec.EncoderException;
import com.feige.api.codec.IByteBuf;
import com.feige.api.session.ISession;

public class TransportCodec implements Codec {

    @Override
    public IByteBuf encode(ISession session, IByteBuf byteBuf, Object obj)  throws EncoderException {
        if (obj instanceof Transport) {
            Transport packet = (Transport) obj;
            byteBuf.writeByte(packet.getBody().length);
            byteBuf.writeByte(packet.getCs());
            byteBuf.writeByte(packet.getVersion());
            byteBuf.writeByte(packet.getCmd());
            byteBuf.writeByte(packet.getSerializeType());
            byteBuf.writeBytes(packet.getSrcId());
            byteBuf.writeBytes(packet.getDestId());
            byteBuf.writeByte(packet.getFeatures());
            byteBuf.writeBytes(packet.getBody());
        }
        return byteBuf;
    }


    @Override
    public Object decode(ISession session, IByteBuf byteBuf)  throws DecoderException {
        byteBuf.markReaderIndex();
        if (byteBuf.readable()){
            byte msgType = byteBuf.readByte();
            
        }
        return null;
    }


    @Override
    public String getKey() {
        return "default";
    }

    @Override
    public boolean isPrimary() {
        return true;
    }
}
