package com.feige.fim.handler;

import com.feige.api.crypto.CipherFactory;
import com.feige.api.msg.FastConnectReq;
import com.feige.api.msg.HandshakeReq;
import com.feige.api.sc.Listener;
import com.feige.fim.api.SessionStorage;
import com.feige.fim.config.ClientConfig;
import com.feige.fim.config.ClientConfigKey;
import com.feige.fim.lg.Logs;
import com.feige.fim.utils.PacketUtils;
import com.feige.utils.common.StringUtils;
import com.feige.framework.annotation.Inject;
import com.feige.utils.spi.annotation.SPI;
import com.feige.api.handler.RemotingException;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.Session;
import com.feige.api.constant.Command;
import com.feige.fim.protocol.Packet;


@SPI(interfaces = SessionHandler.class)
public class ClientSessionHandler extends AbstractSessionHandler {

    @Inject
    private SessionStorage sessionStorage;
    
    @Inject("asymmetricEncryption")
    private CipherFactory asymmetricCipherFactory;
    
    @Inject("symmetricEncryption")
    private CipherFactory symmetricCipherFactory;

    @Override
    public void connected(Session session) throws RemotingException {
        if (ClientConfig.enableCrypto()) {
            session.setCipher(asymmetricCipherFactory.create(new byte[0], ClientConfig.getPublicKey()));
        }
        tryFastConnect(session);
//        handshake(session);
    }
    
    private void tryFastConnect(Session session) throws RemotingException {
        String sessionConfig = sessionStorage.getItem(ClientConfigKey.SESSION_PERSISTENT_KEY);
        if (StringUtils.isBlank(sessionConfig)){
            handshake(session);
            return;
        }
        ClientConfig.deserializeString(sessionConfig);
        if (ClientConfig.isExpired()) {
            sessionStorage.removeItem(ClientConfigKey.SESSION_PERSISTENT_KEY);
            Logs.getInstance().warn("fast connect failure session expired, session=%s", session);
            handshake(session);
            return;
        }
        Packet packet = PacketUtils.createPacket(Command.FAST_CONNECT, FastConnectReq.TYPE);
        FastConnectReq fastConnectReq = PacketUtils.newObject(FastConnectReq.TYPE)
                .setClientId(ClientConfig.getClientId())
                .setSessionId(ClientConfig.getSessionId());
        byte[] serializedObject = PacketUtils.getSerializedObject(fastConnectReq);
        packet.setData(serializedObject);
        session.write(packet, new Listener.CallbackListener((args) -> session.setCipher(symmetricCipherFactory.create(ClientConfig.getClientKey(), ClientConfig.getIv())), Throwable::printStackTrace));
    }
    
    private void handshake(Session session) throws RemotingException {
        Packet packet = PacketUtils.createPacket(Command.HANDSHAKE, HandshakeReq.TYPE);
        HandshakeReq handshakeReq = PacketUtils.newObject(HandshakeReq.TYPE)
                .setClientKey(ClientConfig.getClientKeyString())
                .setIv(ClientConfig.getIvString())
                .setClientVersion(ClientConfig.getClientVersion())
                .setOsName(ClientConfig.getOsName())
                .setOsVersion(ClientConfig.getOsVersion())
                .setClientType(ClientConfig.getClientType())
                .setClientId(ClientConfig.getClientId())
                .setToken(ClientConfig.getToken());
        packet.setData(PacketUtils.getSerializedObject(handshakeReq));
        session.write(packet, new Listener.CallbackListener((args) -> session.setCipher(symmetricCipherFactory.create(ClientConfig.getClientKey(), ClientConfig.getIv())), Throwable::printStackTrace));
    }
}
