package com.feige.fim.handler;


import com.feige.api.handler.MsgHandler;
import com.feige.api.serialize.SerializedClassManager;
import com.feige.framework.annotation.InitMethod;
import com.feige.framework.annotation.Inject;
import com.feige.api.handler.MsgDispatcher;
import com.feige.api.handler.RemotingException;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.framework.aware.ApplicationContextAware;
import com.feige.framework.context.api.ApplicationContext;

import java.util.List;


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

    @Inject
    protected MsgDispatcher<Packet> msgDispatcher;

    @Inject
    private SerializedClassManager serializedClassManager;

    @InitMethod
    public void initializeMsgHandler(){
        List<MsgHandler> msgHandlers = applicationContext.getByType(MsgHandler.class);
        for (MsgHandler msgHandler : msgHandlers) {
            this.msgDispatcher.register(msgHandler);
        }
       
    }
    
    
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
        session.close();
    }

}
