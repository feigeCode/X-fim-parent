package com.feige.fim.handler;

import com.feige.api.crypto.CipherFactory;
import com.feige.api.msg.FastConnect;
import com.feige.api.msg.HandshakeReq;
import com.feige.api.sc.Listener;
import com.feige.fim.api.SessionStorage;
import com.feige.fim.config.ClientConfig;
import com.feige.fim.config.ClientConfigKey;
import com.feige.fim.lg.Logs;
import com.feige.fim.msg.proto.FastConnectReqProto;
import com.feige.fim.msg.proto.HandshakeReqProto;
import com.feige.fim.utils.PacketUtils;
import com.feige.fim.utils.StringUtils;
import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.api.handler.RemotingException;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.Session;
import com.feige.api.constant.Command;
import com.feige.fim.protocol.Packet;
import com.google.auto.service.AutoService;


@AutoService(SessionHandler.class)
@SpiComp
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
        FastConnectReqProto fastConnect = FastConnectReqProto.newBuilder()
                .setClientId(ClientConfig.getClientId())
                .setSessionId(ClientConfig.getSessionId())
                .build();
        Packet packet = PacketUtils.createPacket(Command.FAST_CONNECT, FastConnect.class);
        packet.setData(fastConnect.toByteArray());
        session.write(packet);
        
    }
    
    private void handshake(Session session) throws RemotingException {
        Packet packet = PacketUtils.createPacket(Command.HANDSHAKE, HandshakeReq.class);
        HandshakeReqProto msgProto = HandshakeReqProto.newBuilder()
                .setClientKey(ClientConfig.getClientKeyString())
                .setIv(ClientConfig.getIvString())
                .setClientVersion(ClientConfig.getClientVersion())
                .setOsName(ClientConfig.getOsName())
                .setClientType(ClientConfig.getClientType())
                .setClientId(ClientConfig.getClientId())
                .setToken(ClientConfig.getToken())
                .build();
        packet.setData(msgProto.toByteArray());
        session.write(packet, new Listener() {
            @Override
            public void onSuccess(Object... args) {
                session.setCipher(symmetricCipherFactory.create(ClientConfig.getClientKey(), ClientConfig.getIv()));
                String sessionConfig = ClientConfig.serializeString();
                sessionStorage.setItem(ClientConfigKey.SESSION_PERSISTENT_KEY, sessionConfig);
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
    }
}
