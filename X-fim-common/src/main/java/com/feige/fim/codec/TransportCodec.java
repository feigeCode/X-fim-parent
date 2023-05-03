package com.feige.fim.codec;

import com.feige.api.codec.Codec;
import com.feige.api.codec.IByteBuf;
import com.feige.api.session.ISession;

public class TransportCodec implements Codec {

    @Override
    public IByteBuf encode(ISession session, IByteBuf byteBuf, Object obj)  throws Exception{
        byteBuf.markReaderIndex();
        
        return null;
    }


    @Override
    public Object decode(ISession session, IByteBuf byteBuf)  throws Exception{
        byteBuf.markReaderIndex();
        if (byteBuf.readable()){
            byte msgType = byteBuf.readByte();
            
        }
        return null;
    }


    @Override
    public String getKey() {
        return null;
    }
}
