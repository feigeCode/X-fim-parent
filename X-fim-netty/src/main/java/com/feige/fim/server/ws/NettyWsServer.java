package com.feige.fim.server.ws;

import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.SessionRepository;
import com.feige.fim.server.AbstractNettyServer;

import java.net.InetSocketAddress;

public class NettyWsServer extends AbstractNettyServer {


    public NettyWsServer(InetSocketAddress address, SessionHandler sessionHandler, SessionRepository sessionRepository, Codec codec) {
        super(address, sessionHandler, sessionRepository, codec);
    }

    @Override
    protected void initServerBootstrap() {
        super.initServerBootstrap();
        this.serverBootstrap
                .childHandler(new WsServerInitializer(this, wsPath()));
    }
    

    protected String wsPath(){
        return "/ws";
    }
}
