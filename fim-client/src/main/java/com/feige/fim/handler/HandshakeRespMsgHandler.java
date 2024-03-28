package com.feige.fim.handler;

import com.feige.api.constant.Command;
import com.feige.api.crypto.CipherFactory;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.HandshakeResp;
import com.feige.api.session.Session;
import com.feige.fim.api.SessionStorage;
import com.feige.fim.config.ClientConfig;
import com.feige.fim.config.ClientConfigKey;
import com.feige.fim.protocol.Packet;
import com.feige.fim.utils.PacketUtils;
import com.feige.framework.annotation.CompName;
import com.feige.utils.spi.annotation.SPI;
import lombok.Setter;
import org.bouncycastle.util.encoders.Base64;


@SPI(value="handshake", interfaces = MsgHandler.class)
@Setter
public class HandshakeRespMsgHandler extends AbstractMsgHandler {

    private SessionStorage sessionStorage;

    private CipherFactory symmetricCipherFactory;
    
    @Override
    public byte getCmd() {
        return Command.HANDSHAKE.getCmd();
    }

    @Override
    public void handle(Session session, Packet packet) throws RemotingException {
        HandshakeResp handshakeResp = this.getMsg(packet, HandshakeResp.class);
        long expireTime = handshakeResp.getExpireTime();
        String sessionId = handshakeResp.getSessionId();
        String serverKey = handshakeResp.getServerKey();
        ClientConfig.setClientKey(Base64.decode(serverKey));
        ClientConfig.setSessionId(sessionId);
        ClientConfig.setExpireTime(expireTime);
        String sessionConfig = ClientConfig.serializeString();
        sessionStorage.setItem(ClientConfigKey.SESSION_PERSISTENT_KEY, sessionConfig);
        session.setCipher(symmetricCipherFactory.create(ClientConfig.getClientKey(), ClientConfig.getIv()));
        Packet bindClientPacket = PacketUtils.createBindClientPacket();
        session.write(bindClientPacket);
    }

    @CompName("symmetricEncryption")
    public void setSymmetricCipherFactory(CipherFactory symmetricCipherFactory) {
        this.symmetricCipherFactory = symmetricCipherFactory;
    }
}
