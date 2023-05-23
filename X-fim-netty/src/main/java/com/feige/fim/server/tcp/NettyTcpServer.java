package com.feige.fim.server.tcp;

import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.SessionRepository;
import com.feige.fim.lg.Loggers;
import com.feige.fim.server.AbstractNettyServer;
import org.slf4j.Logger;

import java.net.InetSocketAddress;

public class NettyTcpServer extends AbstractNettyServer {

    public static final Logger LOG = Loggers.SERVER;


    public NettyTcpServer(InetSocketAddress address, SessionHandler sessionHandler, SessionRepository sessionRepository, Codec codec) {
        super(address, sessionHandler, sessionRepository, codec);
    }

    @Override
    public void initServerBootstrap() {
        super.initServerBootstrap();
        this.serverBootstrap
                .childHandler(new TcpServerInitializer(this));
    }
    
}
