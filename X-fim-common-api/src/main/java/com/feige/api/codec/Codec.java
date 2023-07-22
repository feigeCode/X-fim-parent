package com.feige.api.codec;

public interface Codec<T> {
    /**
     * Encoding
     * @param packet packet
     * @param buffer byte buffer
     * @throws EncoderException
     */
    void encode(T packet, Object buffer) throws EncoderException;

    /**
     * Decoding
     * @param byteBuf byte buffer
     * @return object
     * @throws DecoderException 
     */
    T decode(Object byteBuf) throws DecoderException;

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
