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
     * @throws Exception
     */
    IByteBuf encode(ISession session, IByteBuf byteBuf, Object obj) throws Exception;

    /**
     * Decoding
     * @param session session
     * @param byteBuf byte buffer
     * @return object
     * @throws Exception
     */
    Object decode(ISession session, IByteBuf byteBuf) throws Exception;
}
