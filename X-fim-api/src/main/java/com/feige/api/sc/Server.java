package com.feige.api.sc;

import com.feige.api.handler.SessionHandler;

import java.net.InetSocketAddress;


public interface Server extends Service {
    
    /**
     *bind
     * @param bindAddress
     */
    void bind(InetSocketAddress bindAddress);


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

}
