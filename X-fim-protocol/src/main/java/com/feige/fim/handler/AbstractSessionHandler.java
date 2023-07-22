package com.feige.fim.handler;


import com.feige.api.annotation.Inject;
import com.feige.api.cipher.Cipher;
import com.feige.api.handler.MsgDispatcher;
import com.feige.api.handler.RemotingException;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;


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
        if (message instanceof Packet){
            Packet packet = (Packet) message;
            msgDispatcher.dispatch(session, packet);
        }
    }

    @Override
    public void caught(Session session, Throwable exception) throws RemotingException {
        exception.printStackTrace();
    }
}
