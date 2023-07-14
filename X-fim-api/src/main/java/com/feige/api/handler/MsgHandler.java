package com.feige.api.handler;

import com.feige.api.session.Session;

public interface MsgHandler {

    /**
     * handle
     * @param session session
     * @param msg msg
     */
    void handle(Session session, Object msg) throws RemotingException;
    
}
