package com.feige.api.sc;

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

    
    
    @Override
    public boolean isRunning() {
        return ServerState.Started == serverState.get();
    }
}
