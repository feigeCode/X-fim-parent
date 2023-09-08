package com.feige.api.codec;

import com.feige.api.session.Session;


import java.util.List;
import java.util.Set;

public interface Codec {
    /**
     * Encoding
     * @param packet packet
     * @param buffer byte buffer
     * @throws EncoderException
     */
    void encode(Session session, Object packet, Object buffer) throws EncoderException;

    /**
     * encrypt
     * @param packet packet
     * @param session session
     */
    void encrypt(Object packet, Session session);

    /**
     * Decoding
     * @param byteBuf byte buffer
     * @return object
     * @throws DecoderException 
     */
    void decode(Session session, Object byteBuf, List<Object> out) throws DecoderException;

    /**
     * decrypt
     * @param packet packet
     * @param session session
     */
    void decrypt(Object packet, Session session);
    
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

    /**
     * add custom encrypt and decrypt class key
     * 
     */
    void addCustomEncryptAndDecryptClassKey(byte... classKeys);
}
