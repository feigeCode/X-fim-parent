package com.feige.fim.handler;

import com.feige.api.constant.Command;
import com.feige.api.handler.AbstractMsgHandler;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.fim.utils.Pair;
import com.feige.framework.annotation.SpiComp;
import com.google.auto.service.AutoService;

@SpiComp("heartbeat")
@AutoService(MsgHandler.class)
public class HeartbeatMsgHandler extends AbstractMsgHandler<Packet> {
    @Override
    public byte getCmd() {
        return Command.HEARTBEAT.getCmd();
    }

    @Override
    public void handle(Session session, Packet packet) throws RemotingException {
        session.write(Packet.create(Command.HEARTBEAT));
    }

    @Override
    public Class<?> getMsgInterface() {
        return null;
    }

    @Override
    public Pair<Class<?>, Class<?>> getProtoClass() {
        return null;
    }
}
