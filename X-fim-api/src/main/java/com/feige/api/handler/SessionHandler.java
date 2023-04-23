package com.feige.api.handler;

import com.feige.api.session.ISession;
import com.feige.api.spi.Spi;

public interface SessionHandler extends Spi {
    /**
     * on session connected.
     *
     * @param session session.
     */
    void connected(ISession session) throws RemotingException;

    /**
     * on session disconnected.
     *
     * @param session session.
     */
    void disconnected(ISession session) throws RemotingException;

    /**
     * on message sent.
     *
     * @param session session.
     * @param message message.
     */
    void sent(ISession session, Object message) throws RemotingException;

    /**
     * on message received.
     *
     * @param session session.
     * @param message message.
     */
    void received(ISession session, Object message) throws RemotingException;

    /**
     * on exception caught.
     *
     * @param session   session.
     * @param exception exception.
     */
    void caught(ISession session, Throwable exception) throws RemotingException;
}
