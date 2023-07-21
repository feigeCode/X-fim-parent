package com.feige.api.handler;

import com.feige.api.session.Session;

public interface MsgDispatcher<T> {
    
    
    void dispatch(Session session, T t) throws RemotingException;
    
    
    void register(MsgHandler<T> msgHandler);


    MsgHandler<T> unregister(byte cmd);

    MsgHandler<T> getMsgHandler(byte cmd);
}
