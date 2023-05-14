package com.feige.api.sc;

import com.feige.api.handler.RemotingException;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.ISession;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author feige<br />
 * @ClassName: AbstractEndpoint <br/>
 * @Description: <br/>
 * @date: 2023/5/13 14:29<br/>
 */
public abstract class AbstractEndpoint implements Endpoint, SessionHandler {
    private final SessionHandler handler;
    
    public enum State {
        /**
         * created
         */
        CREATED,
        /**
         * initialize
         */
        INITIALIZE,
        /**
         * starting
         */
        STARTING,
        /**
         * started
         */
        STARTED,
        /**
         * closing
         */
        CLOSING,
        /**
         * closed
         */
        CLOSED;
    }
    
    protected final AtomicReference<State> state = new AtomicReference<>(State.CREATED);

    
    public AbstractEndpoint(SessionHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        this.handler = handler;
    }

    
    @Override
    public void send(Object message) throws RemotingException {
        send(message, false);
    }

    
    @Override
    public void close() {
        if (!state.compareAndSet(State.STARTED, State.CLOSED)){
            throw new IllegalStateException("The server has not started");
        }
        
    }

    @Override
    public void close(int timeout) {
        close();
    }

    @Override
    public void startClose() {
        if (isClosed()) {
            return;
        }
        if (!state.compareAndSet(State.STARTED, State.CLOSING)) {
            throw new IllegalStateException("The server has not started");
        }
    }

    

    @Override
    public SessionHandler getSessionHandler() {
        return handler;
    }



    @Override
    public boolean isClosed() {
        return State.CLOSED.equals(state.get());
    }

    public boolean isClosing() {
        return State.CLOSING.equals(state.get()) && !isClosed();
    }

    @Override
    public void connected(ISession session) throws RemotingException {
        if (isClosed()) {
            return;
        }
        handler.connected(session);
    }

    @Override
    public void disconnected(ISession session) throws RemotingException {
        handler.disconnected(session);
    }

    @Override
    public void sent(ISession session, Object msg) throws RemotingException {
        if (isClosed()) {
            return;
        }
        handler.sent(session, msg);
    }

    @Override
    public void received(ISession session, Object msg) throws RemotingException {
        if (isClosed()) {
            return;
        }
        handler.received(session, msg);
    }

    @Override
    public void caught(ISession session, Throwable ex) throws RemotingException {
        handler.caught(session, ex);
    }
}
