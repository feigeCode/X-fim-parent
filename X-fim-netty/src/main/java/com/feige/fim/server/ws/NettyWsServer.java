package com.feige.fim.server.ws;

import com.feige.api.handler.SessionHandler;
import com.feige.fim.factory.NettyEventLoopFactory;
import com.feige.fim.server.AbstractNettyServer;
import com.feige.fim.server.tcp.TcpServerInitializer;
import io.netty.channel.ChannelOption;

import java.net.InetSocketAddress;

public class NettyWsServer extends AbstractNettyServer {

    public NettyWsServer(SessionHandler sessionHandler, InetSocketAddress address) {
        super(sessionHandler, address);
    }

    @Override
    protected void initServerBootstrap() {
        this.serverBootstrap
                .group(bossGroup, workGroup)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .channel(NettyEventLoopFactory.createServerSocketChannelClass())
                .childHandler(new WsServerInitializer(getSessionHandler(), wsPath()));
    }
    

    protected String wsPath(){
        return "/ws";
    }
}
