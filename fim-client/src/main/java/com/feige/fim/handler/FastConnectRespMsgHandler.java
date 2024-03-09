package com.feige.fim.handler;

import com.feige.api.constant.Command;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.SuccessResp;
import com.feige.api.session.Session;
import com.feige.fim.api.SessionStorage;
import com.feige.fim.config.ClientConfigKey;
import com.feige.fim.protocol.Packet;
import com.feige.fim.utils.PacketUtils;
import com.feige.framework.annotation.Inject;
import com.feige.handler.AbstractMsgHandler;
import com.feige.utils.spi.annotation.SPI;


@SPI(value="fastConnect", interfaces = MsgHandler.class)
public class FastConnectRespMsgHandler extends AbstractMsgHandler {
    
    @Inject
    private SessionStorage sessionStorage;
    
    @Override
    public byte getCmd() {
        return Command.FAST_CONNECT.getCmd();
    }

    @Override
    public void handle(Session session, Packet packet) throws RemotingException {
        SuccessResp successResp = this.getMsg(packet, SuccessResp.class);
        int statusCode = successResp.getStatusCode();
        if (statusCode == 0){
            sessionStorage.removeItem(ClientConfigKey.SESSION_PERSISTENT_KEY);
            return;
        }
        Packet bindClientPacket = PacketUtils.createBindClientPacket();
        session.write(bindClientPacket);
    }
}
