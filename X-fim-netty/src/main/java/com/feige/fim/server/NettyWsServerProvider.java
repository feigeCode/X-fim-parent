package com.feige.fim.server;

import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Server;
import com.feige.api.sc.ServerProvider;
import com.feige.api.session.SessionRepository;
import com.feige.fim.config.Configs;
import com.feige.fim.server.tcp.NettyTcpServer;
import com.feige.fim.server.ws.NettyWsServer;
import com.feige.fim.spi.SpiLoaderUtils;
import com.feige.fim.utils.StringUtil;

import java.net.InetSocketAddress;

public class NettyWsServerProvider extends AbstractServerProvider {
    
    @Override
    public Server get() {
        InetSocketAddress socketAddress = getAddress();
        SessionHandler sessionHandler = getSessionHandler();
        SessionRepository sessionRepository = getSessionRepository();
        Codec codec = getCodec();
        return new NettyWsServer(socketAddress, sessionHandler, sessionRepository, codec);
    }

    @Override
    public String getKey() {
        return "ws";
    }
}
