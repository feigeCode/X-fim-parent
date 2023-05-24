package com.feige.fim.server;

import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Server;
import com.feige.api.session.SessionRepository;
import com.feige.fim.server.tcp.NettyTcpServer;


import java.net.InetSocketAddress;

public class NettyTcpServerProvider extends AbstractServerProvider {
    
    @Override
    public Server get() {
        InetSocketAddress socketAddress = getAddress();
        SessionHandler sessionHandler = getSessionHandler();
        SessionRepository sessionRepository = getSessionRepository();
        Codec codec = getCodec();
        return new NettyTcpServer(socketAddress, sessionHandler, sessionRepository, codec);
    }

    @Override
    public String getKey() {
        return "tcp";
    }
}
