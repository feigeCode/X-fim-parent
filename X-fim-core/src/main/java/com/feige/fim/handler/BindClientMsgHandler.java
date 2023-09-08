package com.feige.fim.handler;

import com.feige.api.bind.ClientBindInfo;
import com.feige.api.bind.ClientBindManager;
import com.feige.api.constant.Command;
import com.feige.api.constant.Const;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.BindClientReq;
import com.feige.api.session.Session;
import com.feige.api.session.SessionContext;
import com.feige.api.constant.ServerConfigKey;
import com.feige.fim.protocol.Packet;
import com.feige.framework.annotation.Inject;
import com.feige.utils.logger.Loggers;
import com.feige.utils.spi.annotation.SpiComp;
import com.feige.framework.utils.Configs;

import java.util.Objects;

@SpiComp(interfaces = MsgHandler.class)
public class BindClientMsgHandler extends AbstractMsgHandler {
    
    @Inject
    private ClientBindManager clientBindManager;
    
    
    @Override
    public void handle(Session session, Packet msg) throws RemotingException {
        if (!session.isHandshake()){
            // NOT HANDSHAKE
            this.sendErrorPacket(session, msg, ProtocolConst.ErrorCode.NOT_HANDSHAKE, "NOT HANDSHAKE");
            return;
        }
        
        if (session.isBindClient()) {
            // DUPLICATE BIND
            this.sendErrorPacket(session, msg, ProtocolConst.ErrorCode.DUPLICATE_BIND, "DUPLICATE BIND");
            return;
        }

        Long expireTime = (Long)session.getAttr("expireTime");

        if (expireTime == null || System.currentTimeMillis() > expireTime){
            // SESSION EXPIRE 
            this.sendErrorPacket(session, msg, ProtocolConst.ErrorCode.ILLEGAL_SESSION, "SESSION EXPIRE");
            return;
        }
        
        BindClientReq bindClientReq = this.getMsg(msg, BindClientReq.TYPE);
        String sessionId = (String) session.getAttr("sessionId");
        SessionContext sessionContext = (SessionContext) session.getAttr("sessionContext");
        if (sessionContext != null && Objects.equals(bindClientReq.getSessionId(), sessionId) ){
            saveBindClient(session, sessionContext, bindClientReq);
            Loggers.SERVER.info("session id [{}] bind success", bindClientReq.getSessionId() );
        }
    }

    
    private void saveBindClient(Session session, SessionContext sessionContext, BindClientReq bindClientReq){
        // 注册绑定信息
        ClientBindInfo clientBindInfo = new ClientBindInfo();
        clientBindInfo.setClientId(sessionContext.getClientId())
                .setClientVersion(sessionContext.getClientVersion())
                .setClientType(sessionContext.getClientType())
                .setTags(bindClientReq.getTags())
                .setSessionId(session.getId())
                .setHost(Configs.getString(ServerConfigKey.SERVER_TCP_IP_KEY))
                .setPort(Configs.getInt(ServerConfigKey.SERVER_TCP_IP_KEY));
        clientBindManager.register(clientBindInfo);
        // 标记绑定客户端
        session.markBindClient();
        
    }

    private long getSessionExpireTime(){
        return Configs.getLong(ServerConfigKey.SERVER_SESSION_EXPIRE_TIME, Const.DEFAULT_SESSION_EXPIRE_TIME);
    }
    
    @Override
    public byte getCmd() {
        return Command.BIND.getCmd();
    }
}
