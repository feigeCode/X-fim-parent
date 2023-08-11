package com.feige.fim.handler;

import com.feige.api.constant.Command;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.handler.AbstractMsgHandler;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.HandshakeResp;
import com.feige.api.session.Session;
import com.feige.fim.api.SessionStorage;
import com.feige.fim.config.ClientConfig;
import com.feige.fim.config.ClientConfigKey;
import com.feige.fim.msg.proto.HandshakeRespProto;
import com.feige.fim.protocol.Packet;
import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.google.auto.service.AutoService;
import com.google.common.collect.Lists;
import org.bouncycastle.util.encoders.Base64;

import java.util.List;

@SpiComp("handshake")
@AutoService(MsgHandler.class)
public class HandshakeRespMsgHandler extends AbstractMsgHandler<Packet> {
    
    @Inject
    private SessionStorage sessionStorage;
    
    @Override
    public byte getCmd() {
        return Command.HANDSHAKE.getCmd();
    }

    @Override
    public void handle(Session session, Packet packet) throws RemotingException {
        HandshakeResp handshakeResp = serializedClassManager.getDeserializedObject(packet.getSerializerType(), packet.getClassKey(), packet.getData(), HandshakeResp.class);
        long expireTime = handshakeResp.getExpireTime();
        String sessionId = handshakeResp.getSessionId();
        String serverKey = handshakeResp.getServerKey();
        ClientConfig.setClientKey(Base64.decode(serverKey));
        ClientConfig.setSessionId(sessionId);
        ClientConfig.setExpireTime(expireTime);
        String sessionConfig = ClientConfig.serializeString();
        sessionStorage.setItem(ClientConfigKey.SESSION_PERSISTENT_KEY, sessionConfig);

    }


    @Override
    public List<ClassGenerateParam> getClassGenerateParams() {
        return Lists.newArrayList(
                new ClassGenerateParam(ProtocolConst.JSON, HandshakeResp.HANDSHAKE_RESP_CLASS),
                new ClassGenerateParam(ProtocolConst.PROTOCOL_BUFFER, HandshakeResp.HANDSHAKE_RESP_CLASS, HandshakeRespProto.class, HandshakeRespProto.Builder.class)
        );
    }
}
