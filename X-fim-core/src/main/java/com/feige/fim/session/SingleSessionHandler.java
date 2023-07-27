package com.feige.fim.session;

import com.feige.framework.annotation.InitMethod;
import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.SessionHandler;
import com.feige.api.serialize.SerializedClassManager;
import com.feige.api.serialize.Serializer;
import com.feige.framework.context.AppContext;
import com.feige.fim.handler.AbstractSessionHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.google.auto.service.AutoService;

import java.util.List;

@SpiComp("single")
@AutoService(SessionHandler.class)
public class SingleSessionHandler extends AbstractSessionHandler {
    
    @Inject
    private SerializedClassManager serializedClassManager;
    
    @InitMethod
    public void initialize(){
        List<MsgHandler> msgHandlers = AppContext.getAll(MsgHandler.class);
        for (MsgHandler<Packet> msgHandler : msgHandlers) {
            this.msgDispatcher.register(msgHandler);
        }
        List<Serializer> serializers = AppContext.getAll(Serializer.class);
        for (Serializer serializer : serializers) {
            serializedClassManager.register(serializer);
        }
    }

    @Override
    public void received(Session session, Object message) throws RemotingException {
        super.received(session, message);
    }
}
