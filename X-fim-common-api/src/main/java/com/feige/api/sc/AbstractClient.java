package com.feige.api.sc;


import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.Session;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author feige<br />
 * @ClassName: AbstractClient <br/>
 * @Description: <br/>
 * @date: 2023/6/4 15:30<br/>
 */
public abstract class AbstractClient extends ServiceAdapter implements Client {
    protected final AtomicInteger reconnectCnt = new AtomicInteger(0);
    public static final int MAX_RECONNECT_CNT = 3;
    
    protected final AtomicBoolean connected = new AtomicBoolean();
    
    protected Codec codec;
    protected SessionHandler sessionHandler;
    protected InetSocketAddress address;
    protected volatile Session session;

    public AbstractClient(InetSocketAddress address , Codec codec, SessionHandler sessionHandler) {
        this.address = address;
        this.codec = codec;
        this.sessionHandler = sessionHandler;
    }
    

  
    @Override
    public void reconnect() {
        if (reconnectCnt.incrementAndGet() <= MAX_RECONNECT_CNT) {
            this.doReconnect();
        }
    }
    
    
    protected void doReconnect(){
        this.syncStop();
        this.syncStart();
    }

    @Override
    public boolean isConnected() {
        return connected.get();
    }
    

    @Override
    public Codec getCodec() {
        return codec;
    }

    @Override
    public SessionHandler getSessionHandler() {
        return sessionHandler;
    }

    @Override
    public Session getSession() {
        return session;
    }

    public void sessionActive(Session session){
        this.session = session;
    }

    @Override
    public InetSocketAddress getAddress() {
        return address;
    }
}
