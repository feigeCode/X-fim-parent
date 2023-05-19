package com.feige.fim.server.ws;

import com.feige.api.handler.SessionHandler;
import com.feige.fim.server.AbstractNettyServer;

public class NettyWsServer extends AbstractNettyServer {

    public NettyWsServer(SessionHandler sessionHandler) {
        super(sessionHandler);
    }

    @Override
    protected void initServerBootstrap() {
        
    }
}
