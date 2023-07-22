package com.feige.fim.handler;

import com.feige.api.handler.MsgDispatcher;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Command;
import com.feige.fim.protocol.Packet;

public class ClientSessionHandler extends AbstractSessionHandler {

    public ClientSessionHandler(MsgDispatcher<Packet> msgDispatcher) {
        this.msgDispatcher = msgDispatcher;
    }


    @Override
    public void connected(Session session) throws RemotingException {
        Packet packet = Packet.create(Command.HANDSHAKE);
        
        session.write(packet);
    }
}
