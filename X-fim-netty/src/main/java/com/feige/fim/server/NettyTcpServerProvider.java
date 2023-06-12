package com.feige.fim.server;

import com.feige.fim.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Server;
import com.feige.api.session.SessionRepository;
import com.feige.fim.codec.PacketCodec;
import com.feige.fim.config.Configs;
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

    @Override
    protected Codec getCodec(){
        return new PacketCodec(65535, (byte) -33, (byte) 1, 10, "default");
    }
}
