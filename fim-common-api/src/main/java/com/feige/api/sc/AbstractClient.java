package com.feige.api.sc;


import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.SessionRepository;

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
    protected SessionRepository sessionRepository;
    protected InetSocketAddress address;

    public AbstractClient(InetSocketAddress address, Codec codec, SessionHandler sessionHandler, SessionRepository sessionRepository) {
        this.address = address;
        this.codec = codec;
        this.sessionHandler = sessionHandler;
        this.sessionRepository = sessionRepository;
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
    public void setAddress(InetSocketAddress remoteAddress) {
        if (remoteAddress.equals(this.address)){
            return;
        }
        this.address = remoteAddress;
        this.doReconnect();
    }

    @Override
    public InetSocketAddress getAddress() {
        return address;
    }
}
