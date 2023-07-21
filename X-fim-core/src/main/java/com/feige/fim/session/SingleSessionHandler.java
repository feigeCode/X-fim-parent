package com.feige.fim.session;

import com.feige.api.annotation.SpiComp;
import com.feige.fim.handler.AbstractSessionHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;

@SpiComp("single")
public class SingleSessionHandler extends AbstractSessionHandler {
    

    @Override
    public void received(Session session, Object message) throws RemotingException {
        super.received(session, message);
    }
}
