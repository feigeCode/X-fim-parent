package com.feige.fim.server;

import com.feige.api.constant.Const;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.AbstractServer;
import com.feige.api.sc.Listener;
import com.feige.fim.config.Configs;
import com.feige.fim.factory.NettyEventLoopFactory;
import com.feige.fim.lg.Loggers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;

import java.net.InetSocketAddress;


public abstract class AbstractNettyServer extends AbstractServer {
    public static final Logger LOG = Loggers.SERVER;
    protected EventLoopGroup bossGroup;
    protected EventLoopGroup workGroup;
    protected ServerBootstrap serverBootstrap;
    protected Channel channel;

    public AbstractNettyServer(SessionHandler sessionHandler) {
        super(sessionHandler);
    }

    @Override
    public void initialize() {
        this.bossGroup = NettyEventLoopFactory.createEventLoopGroup(Const.DEFAULT_IO_THREADS, "fim-tcp-server-boss-");
        this.workGroup = NettyEventLoopFactory.createEventLoopGroup(Const.DEFAULT_IO_THREADS, "fim-tcp-server-work-");
        this.serverBootstrap = new ServerBootstrap();
    }

    @Override
    protected void doStart(Listener listener) {
        this.channel.newSucceededFuture().addListener(future -> {
            if (future.isSuccess()) {
                LOG.info("netty tcp server in {} port start finish....", Configs.getInt(Configs.ConfigKey.SERVER_TCP_PORT_KEY));
            }else {
                LOG.error("netty tcp server in {} port start fail....", Configs.getInt(Configs.ConfigKey.SERVER_TCP_PORT_KEY));
            }
        });
        this.channel.closeFuture().addListener(future -> this.stop());
    }

    @Override
    protected void doStop(Listener listener) {
        if (bossGroup != null && !(bossGroup.isShuttingDown() && bossGroup.isShutdown() && bossGroup.isTerminated())){
            bossGroup.shutdownGracefully();
        }
        if (workGroup != null && !(workGroup.isShuttingDown() && workGroup.isShutdown() && workGroup.isTerminated())){
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void bind(InetSocketAddress bindAddress) {
        this.bindAddress = bindAddress;
        initServerBootstrap();
        ChannelFuture channelFuture = this.serverBootstrap
                .bind(bindAddress)
                .syncUninterruptibly();
        this.channel = channelFuture.channel();
    }
    
    protected abstract void initServerBootstrap();
}
