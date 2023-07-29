package com.feige.fim.session;


import com.feige.framework.annotation.SpiComp;
import com.feige.api.handler.SessionHandler;
import com.feige.fim.handler.AbstractSessionHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.google.auto.service.AutoService;



@SpiComp("single")
@AutoService(SessionHandler.class)
public class SingleSessionHandler extends AbstractSessionHandler {

    @Override
    public void received(Session session, Object message) throws RemotingException {
        super.received(session, message);
    }
}
