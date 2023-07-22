package com.feige.fim.session;

import com.feige.api.annotation.InitMethod;
import com.feige.api.annotation.SpiComp;
import com.feige.api.handler.MsgHandler;
import com.feige.fim.context.AppContext;
import com.feige.fim.handler.AbstractSessionHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;

import java.util.List;

@SpiComp("single")
public class SingleSessionHandler extends AbstractSessionHandler {
    
    @InitMethod
    public void initialize(){
        List<MsgHandler> msgHandlers = AppContext.getAll(MsgHandler.class);
        for (MsgHandler<Packet> msgHandler : msgHandlers) {
            this.msgDispatcher.register(msgHandler);
        }
    }

    @Override
    public void received(Session session, Object message) throws RemotingException {
        super.received(session, message);
    }
}
