package com.feige.fim.codec;

import com.feige.api.codec.Codec;
import com.feige.api.codec.IByteBuf;
import com.feige.api.session.ISession;

public class TransportCodec implements Codec {

    @Override
    public IByteBuf encode(ISession session, IByteBuf byteBuf, Object obj) {
        
        return null;
    }


    @Override
    public Object decode(ISession session, IByteBuf byteBuf) {
        return null;
    }


    @Override
    public String getKey() {
        return null;
    }
}
