package com.feige.fim.handler;

import com.feige.api.handler.AbstractMsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Command;
import com.feige.fim.protocol.Packet;

public class HeartbeatMsgHandler extends AbstractMsgHandler {
    @Override
    public byte getCmd() {
        return Command.HEARTBEAT.getCmd();
    }

    @Override
    public void handle(Session session, Object msg) throws RemotingException {
        session.write(Packet.create(Command.HEARTBEAT));
    }
}
