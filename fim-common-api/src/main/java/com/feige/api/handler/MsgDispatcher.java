package com.feige.api.handler;

import com.feige.api.session.Session;

public interface MsgDispatcher<T> {
    
    
    void dispatch(Session session, T t) throws RemotingException;
    
    
    void register(MsgHandler msgHandler);


    MsgHandler unregister(byte cmd);

    MsgHandler getMsgHandler(byte cmd);
}
