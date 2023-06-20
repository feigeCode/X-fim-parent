package com.feige.fim.session;

import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.fim.spi.SpiLoaderUtils;

public class SingleSessionHandler extends AbstractSessionHandler {
    
    @Override
    public String getKey() {
        return "single";
    }

    @Override
    public void received(Session session, Object message) throws RemotingException {
        if (message instanceof Packet){
            Packet packet = (Packet) message;
            String cmd = String.valueOf(packet.getCmd());
            MsgHandler msgHandler = SpiLoaderUtils.get(cmd, MsgHandler.class);
            msgHandler.handle(session, message);
        }
    }
}
