package com.feige.api.sc;

import com.feige.fim.codec.Codec;
import com.feige.api.handler.SessionHandler;
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

    
    public enum ServerState {Created, Initialized, Starting, Started, Shutdown}

    protected final AtomicReference<ServerState> serverState = new AtomicReference<>(ServerState.Created);

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
        return ServerState.Started == serverState.get();
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
