package com.feige.api.codec;

import com.feige.api.annotation.CacheOne;
import com.feige.api.session.Session;
import com.feige.api.spi.Spi;

@CacheOne
public interface Codec extends Spi {

    /**
     * Encoding
     * @param session session
     * @param byteBuf byte buffer
     * @param obj object
     * @return byte buffer
     * @throws EncoderException
     */
    IByteBuf encode(Session session, IByteBuf byteBuf, Object obj) throws EncoderException;

    /**
     * Decoding
     * @param session session
     * @param byteBuf byte buffer
     * @return object
     * @throws DecoderException
     */
    Object decode(Session session, IByteBuf byteBuf) throws DecoderException;

}
