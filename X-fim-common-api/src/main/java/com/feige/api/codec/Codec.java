package com.feige.api.codec;

import com.feige.api.session.Session;

import java.util.List;

public interface Codec {
    /**
     * Encoding
     * @param packet packet
     * @param buffer byte buffer
     * @throws EncoderException
     */
    void encode(Session session, Object packet, Object buffer) throws EncoderException;

    /**
     * Decoding
     * @param byteBuf byte buffer
     * @return object
     * @throws DecoderException 
     */
    void decode(Session session, Object byteBuf, List<Object> out) throws DecoderException;

    /**
     * version
     * @return
     */
    byte getVersion();

    /**
     * heartbeat
     * @return
     */
    byte getHeartbeat();

    /**
     * max packet size
     * @return
     */
    int getMaxPacketSize();

    /**
     * protocol header length
     * @return
     */
    int getHeaderLength();

    /**
     * check sum
     * @return
     */
    ICheckSum getCheckSum();
}
