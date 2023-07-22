package com.feige.fim.handler;

import com.feige.api.annotation.SpiComp;
import com.feige.api.handler.AbstractMsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Command;
import com.feige.fim.protocol.Packet;

@SpiComp("heartbeat")
public class HeartbeatMsgHandler extends AbstractMsgHandler<Packet> {
    @Override
    public byte getCmd() {
        return Command.HEARTBEAT.getCmd();
    }

    @Override
    public void handle(Session session, Packet packet) throws RemotingException {
        session.write(Packet.create(Command.HEARTBEAT));
    }
}
