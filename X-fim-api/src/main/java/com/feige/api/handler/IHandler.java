package com.feige.api.handler;

import com.feige.api.session.ISession;

public interface IHandler {
    
    void handle(ISession session, Object msg);
}
