package com.feige.api.handler;

import com.feige.api.session.Session;
import com.feige.api.spi.Spi;

public interface SessionHandler extends Spi {
    /**
     * on session connected.
     *
     * @param session session.
     */
    void connected(Session session) throws RemotingException;

    /**
     * on session disconnected.
     *
     * @param session session.
     */
    void disconnected(Session session) throws RemotingException;
    
    /**
     * on message received.
     *
     * @param session session.
     * @param message message.
     */
    void received(Session session, Object message) throws RemotingException;

    /**
     * on exception caught.
     *
     * @param session   session.
     * @param exception exception.
     */
    void caught(Session session, Throwable exception) throws RemotingException;
}
