package com.feige.api.sc;

import com.feige.api.handler.SessionHandler;
import com.feige.api.session.SessionRepository;

/**
 * @author feige<br />
 * @ClassName: AbstractServer <br/>
 * @Description: <br/>
 * @date: 2023/5/13 14:33<br/>
 */
public abstract class AbstractServer extends AbstractEndpoint implements Server{
    
    private final SessionRepository sessionRepository;
    public AbstractServer(SessionHandler handler) {
        super(handler);
        this.sessionRepository = null;
    }

    protected abstract void doOpen() throws Throwable;

    protected abstract void doClose() throws Throwable;

    protected abstract int getSessionsSize();
}
