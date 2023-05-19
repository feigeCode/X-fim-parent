package com.feige.api.sc;

import com.feige.api.handler.SessionHandler;

import java.net.InetSocketAddress;


public interface Server extends Service {
    

    
    /**
     * get session handler.
     *
     * @return session handler
     */
    SessionHandler getSessionHandler();

    /**
     * get address.
     *
     * @return address.
     */
    InetSocketAddress getAddress();
    

}
