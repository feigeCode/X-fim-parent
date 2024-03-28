package com.feige.fim.handler;


import com.feige.api.handler.MsgDispatcher;
import com.feige.api.handler.RemotingException;
import com.feige.api.handler.SessionHandler;
import com.feige.api.serialize.SerializedClassManager;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.framework.aware.ApplicationContextAware;
import com.feige.framework.context.api.ApplicationContext;
import lombok.Setter;


/**
 * @author feige<br />
 * @ClassName: AbstractSessionHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/21 17:06<br/>
 */
public abstract class AbstractSessionHandler implements SessionHandler, ApplicationContextAware {

    protected ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Setter
    protected MsgDispatcher<Packet> msgDispatcher;

    @Setter
    private SerializedClassManager serializedClassManager;


    
    
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
