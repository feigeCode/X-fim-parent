package com.feige.fim.handler;

import com.feige.api.constant.Command;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.SuccessResp;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.utils.spi.annotation.SPI;

@SPI(interfaces = MsgHandler.class)
public class BindClientRespMsgHandler extends AbstractMsgHandler {
    @Override
    public void handle(Session session, Packet msg) throws RemotingException {
        SuccessResp successResp = getMsg(msg, SuccessResp.class);
        System.out.println(successResp);
    }

    @Override
    public byte getCmd() {
        return Command.BIND.getCmd();
    }
}
