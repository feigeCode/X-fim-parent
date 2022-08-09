package com.feige.api.codec;

import com.feige.api.session.ISession;

public interface Decoder {

    /**
     * 解码
     * @param session
     * @param byteBuf
     * @return
     */
    Object decode(ISession session, IByteBuf byteBuf);

}
