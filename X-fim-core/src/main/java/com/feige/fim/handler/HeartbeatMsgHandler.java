package com.feige.fim.handler;

import com.feige.framework.annotation.SpiComp;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.api.constant.Command;
import com.feige.fim.protocol.Packet;



@SpiComp(value="heartbeat", interfaces = MsgHandler.class)
public class HeartbeatMsgHandler extends AbstractMsgHandler {
    @Override
    public byte getCmd() {
        return Command.HEARTBEAT.getCmd();
    }

    @Override
    public void handle(Session session, Packet packet) throws RemotingException {
        session.write(Packet.create(Command.HEARTBEAT));
    }
    
}
