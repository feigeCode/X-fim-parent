package com.feige.api.sc;

import com.feige.api.handler.RemotingException;
import com.feige.api.handler.SessionHandler;

import java.net.InetSocketAddress;

/**
 * @author feige<br />
 * @ClassName: Endpoint <br/>
 * @Description: <br/>
 * @date: 2023/5/13 14:19<br/>
 */
public interface Endpoint {
    

    /**
     * get session handler.
     *
     * @return session handler
     */
    SessionHandler getSessionHandler();

    /**
     * get local address.
     *
     * @return local address.
     */
    InetSocketAddress getLocalAddress();

    /**
     * send message.
     *
     * @param message
     * @throws RemotingException
     */
    void send(Object message) throws RemotingException;

    /**
     * send message.
     *
     * @param message message
     * @param sent    already sent to socket?
     */
    void send(Object message, boolean sent) throws RemotingException;

    /**
     * close the channel.
     */
    void close();

    /**
     * Graceful close the channel.
     */
    void close(int timeout);

    void startClose();

    /**
     * is closed.
     *
     * @return closed
     */
    boolean isClosed();
    
    boolean isRunning();
}
