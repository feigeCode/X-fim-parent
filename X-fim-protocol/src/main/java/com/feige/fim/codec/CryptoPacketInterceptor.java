package com.feige.fim.codec;

import com.feige.api.codec.DecoderException;
import com.feige.api.codec.EncoderException;
import com.feige.api.codec.PacketInterceptor;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.crypto.Cipher;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.framework.utils.Configs;
import com.feige.utils.spi.annotation.SpiComp;

@SpiComp(interfaces = PacketInterceptor.class)
public class CryptoPacketInterceptor implements PacketInterceptor {

    @Override
    public void writePacket(Session session, Object packet) {
        this.encrypt(packet, session);
    }

    private void encrypt(Object obj, Session session){
        Packet packet = (Packet) obj;
        if (ProtocolConst.SerializedClass.isCustomCrypto(packet.getRealType())) {
            return;
        }
        Boolean enable = Configs.getBoolean(Configs.ConfigKey.CRYPTO_ENABLE, false);
        if (enable){
            Cipher cipher = session.getCipher();
            if (cipher == null){
                throw new EncoderException("cipher is null");
            }
            byte[] data = packet.getData();
            byte[] encryptData = cipher.encrypt(data);
            packet.setData(encryptData);
            packet.addFeature(ProtocolConst.ENCRYPT);
        }
    }

    @Override
    public void readPacket(Session session, Object packet) {
        this.decrypt(packet, session);
    }

    private void decrypt(Object obj, Session session){
        Packet packet = (Packet) obj;
        if (ProtocolConst.SerializedClass.isCustomCrypto(packet.getRealType())) {
            return;
        }
        if (packet.hasFeature(ProtocolConst.ENCRYPT)) {
            Cipher cipher = session.getCipher();
            if (cipher == null) {
                throw new DecoderException("cipher is null");
            }
            packet.setData(cipher.decrypt(packet.getData()));
        }
    }
    
    @Override
    public int order() {
        return 0;
    }
}
