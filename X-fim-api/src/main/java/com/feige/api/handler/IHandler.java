package com.feige.api.handler;

import com.feige.api.session.Session;

public interface IHandler {
    
    void handle(Session session, Object msg);
}
