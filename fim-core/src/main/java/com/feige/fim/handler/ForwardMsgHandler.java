package com.feige.fim.handler;

import com.feige.api.constant.ProtocolConst;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.api.session.SessionRepository;
import com.feige.fim.bind.ClientBindInfo;
import com.feige.fim.bind.ClientBindManager;
import com.feige.fim.protocol.Packet;
import lombok.Setter;

import java.util.Map;

public abstract class ForwardMsgHandler extends AbstractMsgHandler {

    @Setter
    protected SessionRepository sessionRepository;

    @Setter
    protected ClientBindManager clientBindManager;


    @Override
    public void handle(Session session, Packet packet) throws RemotingException {
        if (!session.isBindClient()) {
            this.sendErrorPacket(session, packet, ProtocolConst.ErrorCode.NOT_BIND, "NOT BIND");
            return;
        }
        String receiverId = this.getReceiverId(packet);
        Map<String, ClientBindInfo> clientBindInfoMap = clientBindManager.lookupAll(receiverId);
        if (clientBindInfoMap == null || clientBindInfoMap.isEmpty()) {
            return;
        }
        for (ClientBindInfo bindInfo : clientBindInfoMap.values()) {
            sessionRepository.write(bindInfo.getSessionId(), packet);
        }
    }


    public abstract String getReceiverId(Packet packet);



}
