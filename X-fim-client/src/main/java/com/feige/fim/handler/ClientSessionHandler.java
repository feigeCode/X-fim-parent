package com.feige.fim.handler;

import com.feige.api.crypto.CipherFactory;
import com.feige.api.msg.FastConnectReq;
import com.feige.api.msg.HandshakeReq;
import com.feige.api.sc.Listener;
import com.feige.api.serialize.SerializedClassManager;
import com.feige.fim.api.SessionStorage;
import com.feige.fim.config.ClientConfig;
import com.feige.fim.config.ClientConfigKey;
import com.feige.fim.lg.Logs;
import com.feige.fim.msg.proto.FastConnectReqProto;
import com.feige.fim.msg.proto.HandshakeReqProto;
import com.feige.fim.utils.PacketUtils;
import com.feige.fim.utils.Pair;
import com.feige.fim.utils.StringUtils;
import com.feige.framework.annotation.InitMethod;
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
    
    @Inject
    private SerializedClassManager serializedClassManager;
    
    @InitMethod
    public void initMsgClass(){
        serializedClassManager.getClass(ClientConfig.getSerializerType(), FastConnectReq.class, () -> new Object[]{FastConnectReqProto.class, FastConnectReqProto.Builder.class});
        serializedClassManager.getClass(ClientConfig.getSerializerType(), HandshakeReq.class, () -> new Object[]{HandshakeReqProto.class, HandshakeReqProto.Builder.class});
    }
    
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
        Pair<Packet, FastConnectReq> packetPair = PacketUtils.createPacketPair(Command.FAST_CONNECT, FastConnectReq.TYPE);
        FastConnectReq fastConnectReq = packetPair.getV()
                .setClientId(ClientConfig.getClientId())
                .setSessionId(ClientConfig.getSessionId());
        byte[] serializedObject = PacketUtils.getSerializedObject(fastConnectReq);
        Packet packet = packetPair.getK();
        packet.setData(serializedObject);
        session.write(packet);
    }
    
    private void handshake(Session session) throws RemotingException {
        Pair<Packet, HandshakeReq> packetPair = PacketUtils.createPacketPair(Command.HANDSHAKE, HandshakeReq.TYPE);
        HandshakeReq handshakeReq = packetPair.getV()
                .setClientKey(ClientConfig.getClientKeyString())
                .setIv(ClientConfig.getIvString())
                .setClientVersion(ClientConfig.getClientVersion())
                .setOsName(ClientConfig.getOsName())
                .setClientType(ClientConfig.getClientType())
                .setClientId(ClientConfig.getClientId())
                .setToken(ClientConfig.getToken());
        Packet packet = packetPair.getK();
        packet.setData(PacketUtils.getSerializedObject(handshakeReq));
        session.write(packet, new Listener() {
            @Override
            public void onSuccess(Object... args) {
                session.setCipher(symmetricCipherFactory.create(ClientConfig.getClientKey(), ClientConfig.getIv()));
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
    }
}
