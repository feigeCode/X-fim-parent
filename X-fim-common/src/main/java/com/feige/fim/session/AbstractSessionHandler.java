package com.feige.fim.session;

import com.feige.api.handler.RemotingException;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.Session;

/**
 * @author feige<br />
 * @ClassName: AbstractSessionHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/21 17:06<br/>
 */
public abstract class AbstractSessionHandler implements SessionHandler {
    
    @Override
    public void connected(Session session) throws RemotingException {
        
    }

    @Override
    public void disconnected(Session session) throws RemotingException {
        
    }
    
    

    @Override
    public void received(Session session, Object message) throws RemotingException {
        System.out.println(message);
    }

    @Override
    public void caught(Session session, Throwable exception) throws RemotingException {
        exception.printStackTrace();
    }
    
}
