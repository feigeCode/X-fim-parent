package com.feige.api.sc;

import com.feige.api.handler.SessionHandler;

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

    protected InetSocketAddress bindAddress;
    
    public AbstractServer(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
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
    public InetSocketAddress getLocalAddress() {
        return bindAddress;
    }
}
