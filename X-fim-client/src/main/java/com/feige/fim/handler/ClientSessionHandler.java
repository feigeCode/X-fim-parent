package com.feige.fim.handler;

import com.feige.api.msg.FastConnect;
import com.feige.api.msg.Handshake;
import com.feige.fim.api.SessionStorage;
import com.feige.fim.config.ClientConfig;
import com.feige.fim.config.ClientConfigKey;
import com.feige.fim.lg.Logs;
import com.feige.fim.msg.proto.FastConnectMsgProto;
import com.feige.fim.msg.proto.HandshakeMsgProto;
import com.feige.fim.utils.AssertUtil;
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
    
    
    @Override
    public void connected(Session session) throws RemotingException {
        tryFastConnect(session);
    }
    
    private void tryFastConnect(Session session) throws RemotingException {
        String sessionConfig = sessionStorage.getItem(ClientConfigKey.SESSION_PERSISTENT_KEY);
        ClientConfig clientConfig = applicationContext.get(ClientConfig.class);
        AssertUtil.notNull(clientConfig, "clientConfig");
        if (StringUtils.isBlank(sessionConfig)){
            handshake(session, clientConfig);
            return;
        }
        if (clientConfig.isExpired()) {
            sessionStorage.removeItem(ClientConfigKey.SESSION_PERSISTENT_KEY);
            Logs.getInstance().warn("fast connect failure session expired, session=%s", session);
            handshake(session, clientConfig);
            return;
        }
        FastConnectMsgProto fastConnect = FastConnectMsgProto.newBuilder()
                .setClientId(clientConfig.getClientId())
                .setSessionId(clientConfig.getSessionId())
                .build();
        Packet packet = PacketUtils.createPacket(Command.FAST_CONNECT, FastConnect.class);
        packet.setData(fastConnect.toByteArray());
        session.write(packet);
    }
    
    private void handshake(Session session, ClientConfig clientConfig) throws RemotingException {
        Packet packet = PacketUtils.createPacket(Command.HANDSHAKE, Handshake.class);
        HandshakeMsgProto msgProto = HandshakeMsgProto.newBuilder()
                .setClientKey(clientConfig.getClientKeyString())
                .setIv(clientConfig.getIvString())
                .setClientVersion(clientConfig.getClientVersion())
                .setOsName(clientConfig.getOsName())
                .setOsCode(clientConfig.getOsCode())
                .setClientId(clientConfig.getClientId())
                .setToken(clientConfig.getToken())
                .build();
        packet.setData(msgProto.toByteArray());
        session.write(packet);
    }
}
