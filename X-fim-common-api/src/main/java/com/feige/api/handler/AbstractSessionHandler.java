package com.feige.api.handler;


import com.feige.api.annotation.Inject;
import com.feige.api.session.Session;
import com.sun.xml.internal.ws.api.message.Packet;

/**
 * @author feige<br />
 * @ClassName: AbstractSessionHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/21 17:06<br/>
 */
public abstract class AbstractSessionHandler implements SessionHandler {
    
    @Inject
    protected MsgDispatcher<Packet> msgDispatcher;
    
    @Override
    public void connected(Session session) throws RemotingException {
        
    }

    @Override
    public void disconnected(Session session) throws RemotingException {
        
    }
    
    

    @Override
    public void received(Session session, Object message) throws RemotingException {
        msgDispatcher.dispatch(session, (Packet) message);
    }

    @Override
    public void caught(Session session, Throwable exception) throws RemotingException {
        exception.printStackTrace();
    }
    
}
