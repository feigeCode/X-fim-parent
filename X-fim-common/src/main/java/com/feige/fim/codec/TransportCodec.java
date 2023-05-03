package com.feige.fim.codec;

import com.feige.api.codec.Codec;
import com.feige.api.codec.DecoderException;
import com.feige.api.codec.EncoderException;
import com.feige.api.codec.IByteBuf;
import com.feige.api.session.ISession;

public class TransportCodec implements Codec {

    @Override
    public IByteBuf encode(ISession session, IByteBuf byteBuf, Object obj)  throws EncoderException {
        byteBuf.markReaderIndex();
        
        return null;
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
