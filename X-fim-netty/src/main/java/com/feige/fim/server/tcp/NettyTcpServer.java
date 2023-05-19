package com.feige.fim.server.tcp;

import com.feige.api.handler.SessionHandler;
import com.feige.fim.factory.NettyEventLoopFactory;
import com.feige.fim.lg.Loggers;
import com.feige.fim.server.AbstractNettyServer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import org.slf4j.Logger;

import java.net.InetSocketAddress;

public class NettyTcpServer extends AbstractNettyServer {

    public static final Logger LOG = Loggers.SERVER;


    public NettyTcpServer(SessionHandler handler, InetSocketAddress address) {
        super(handler, address);
    }

    
    @Override
    public void initServerBootstrap() {
        this.serverBootstrap
                .group(bossGroup, workGroup)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .channel(NettyEventLoopFactory.createServerSocketChannelClass())
                .childHandler(new TcpServerInitializer(getSessionHandler()));
    }
    
}
