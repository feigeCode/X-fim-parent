package com.feige.fim.server.ws;

import com.feige.fim.codec.Codec;
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


    @Override
    protected String getBossGroupThreadName() {
        return "fim-ws-server-boss-";
    }

    @Override
    protected String getWorkerGroupThreadName() {
        return "fim-ws-server-work-";
    }

    @Override
    protected int getBossGroupThreadNum() {
        return super.getBossGroupThreadNum();
    }

    @Override
    protected int getWorkerGroupThreadNum() {
        return super.getWorkerGroupThreadNum();
    }
}
