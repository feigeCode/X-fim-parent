package com.feige.fim.session;

import com.feige.api.handler.RemotingException;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.ISession;
import com.feige.api.session.SessionRepository;

/**
 * @author feige<br />
 * @ClassName: AbstractSessionHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/21 17:06<br/>
 */
public abstract class AbstractSessionHandler implements SessionHandler {
    private SessionRepository sessionRepository;

    @Override
    public void connected(ISession session) throws RemotingException {
        sessionRepository.add(session);
    }

    @Override
    public void disconnected(ISession session) throws RemotingException {
        sessionRepository.remove(session);
    }

    @Override
    public void sent(ISession session, Object message) throws RemotingException {
        
    }
    

    @Override
    public void received(ISession session, Object message) throws RemotingException {
        
    }

    @Override
    public void caught(ISession session, Throwable exception) throws RemotingException {
        
    }
    
}
