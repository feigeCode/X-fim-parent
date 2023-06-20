package com.feige.api.handler;

import com.feige.api.session.Session;
import com.feige.api.spi.Spi;

public interface MsgHandler extends Spi {

    /**
     * handle
     * @param session session
     * @param msg msg
     */
    void handle(Session session, Object msg) throws RemotingException;
    
}
