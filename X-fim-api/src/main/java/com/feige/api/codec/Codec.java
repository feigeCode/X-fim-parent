package com.feige.api.codec;

import com.feige.api.session.ISession;
import com.feige.api.spi.Spi;

public interface Codec extends Spi {

    /**
     * Encoding
     * @param session session
     * @param byteBuf byte buffer
     * @param obj object
     * @return byte buffer
     */
    IByteBuf encode(ISession session, IByteBuf byteBuf, Object obj);

    /**
     * Decoding
     * @param session session
     * @param byteBuf byte buffer
     * @return object
     */
    Object decode(ISession session, IByteBuf byteBuf);
}
