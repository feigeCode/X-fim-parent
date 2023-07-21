package com.feige.api.handler;

import com.feige.api.session.Session;

public interface MsgHandler<T> {

    /**
     * handle
     * @param session session
     * @param msg msg
     */
    void handle(Session session, T msg) throws RemotingException;


    /**
     * cmd
     * @return cmd
     */
    byte getCmd();
    
}
