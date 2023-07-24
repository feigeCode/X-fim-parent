package com.feige.fim.handler;

import com.feige.annotation.SpiComp;
import com.feige.api.handler.AbstractMsgDispatcher;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;

@SpiComp
public class PacketDispatcher extends AbstractMsgDispatcher<Packet> {
    
    @Override
    public void dispatch(Session session, Packet packet) throws RemotingException {
        byte cmd = packet.getCmd();
        MsgHandler<Packet> msgHandler = this.getMsgHandler(cmd);
        if (msgHandler != null){
            msgHandler.handle(session, packet);
            return;
        }
        throw new RemotingException(session, "cmd="+ cmd + "not found handler");
    }
}
