package com.feige.fim.handler;

import com.feige.annotation.SpiComp;
import com.feige.api.handler.RemotingException;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.Session;
import com.feige.api.constant.Command;
import com.feige.fim.protocol.Packet;
import com.google.auto.service.AutoService;

@AutoService(SessionHandler.class)
@SpiComp
public class ClientSessionHandler extends AbstractSessionHandler {
    
    @Override
    public void connected(Session session) throws RemotingException {
        Packet packet = Packet.create(Command.HANDSHAKE);
        
        session.write(packet);
    }
}
