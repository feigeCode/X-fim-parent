package com.feige.fim.handler;

import com.feige.api.constant.Command;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.FastConnectResp;
import com.feige.api.session.Session;
import com.feige.fim.api.SessionStorage;
import com.feige.fim.config.ClientConfigKey;
import com.feige.fim.protocol.Packet;
import com.feige.fim.utils.PacketUtils;
import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;


@SpiComp(value="fastConnect", interfaces = MsgHandler.class)
public class FastConnectRespMsgHandler extends AbstractMsgHandler {
    
    @Inject
    private SessionStorage sessionStorage;
    
//    @Inject
//    private Client client;
    
    @Override
    public byte getCmd() {
        return Command.FAST_CONNECT.getCmd();
    }

    @Override
    public void handle(Session session, Packet packet) throws RemotingException {
        FastConnectResp fastConnectResp = this.getMsg(packet, FastConnectResp.class);
        int statusCode = fastConnectResp.getStatusCode();
        if (statusCode == 0){
            sessionStorage.removeItem(ClientConfigKey.SESSION_PERSISTENT_KEY);
//            client.reconnect();
            return;
        }
        Packet bindClientPacket = PacketUtils.createBindClientPacket();
        session.write(bindClientPacket);
    }
}
