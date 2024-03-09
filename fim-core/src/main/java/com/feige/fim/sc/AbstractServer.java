package com.feige.fim.sc;


import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.ServiceAdapter;
import com.feige.api.session.SessionRepository;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author feige<br />
 * @ClassName: AbstractServer <br/>
 * @Description: <br/>
 * @date: 2023/5/13 14:33<br/>
 */
public abstract class AbstractServer extends ServiceAdapter implements Server {

    
    public enum ServerState {CREATED, INITIALIZED, STARTING, STARTED, SHUTDOWN}

    protected final AtomicReference<ServerState> serverState = new AtomicReference<>(ServerState.CREATED);

    protected SessionHandler sessionHandler;
    
    protected Codec codec;
    
    protected SessionRepository sessionRepository;

    protected InetSocketAddress address;

    
    public AbstractServer(InetSocketAddress address, SessionHandler sessionHandler, SessionRepository sessionRepository, Codec codec) {
        this.address = address;
        this.sessionHandler = sessionHandler;
        this.sessionRepository = sessionRepository;
        this.codec = codec;
    }
    
    

    @Override
    public boolean isRunning() {
        return ServerState.STARTED == serverState.get();
    }
    

    @Override
    public SessionHandler getSessionHandler() {
        return sessionHandler;
    }

    @Override
    public InetSocketAddress getAddress() {
        return address;
    }

    @Override
    public Codec getCodec() {
        return codec;
    }
    @Override
    public SessionRepository getSessionRepository() {
        return sessionRepository;
    }
}
