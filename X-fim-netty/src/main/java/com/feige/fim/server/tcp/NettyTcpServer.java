package com.feige.fim.server.tcp;

import com.feige.fim.config.Configs;
import com.feige.api.constant.Const;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.IServer;
import com.feige.fim.factory.NettyEventLoopFactory;
import com.feige.fim.spi.SpiLoader;
import org.slf4j.Logger;
import com.feige.fim.lg.Loggers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;

import java.net.InetSocketAddress;
import java.util.Map;

public class NettyTcpServer implements IServer {

    public static final Logger LOG = Loggers.SERVER;

    private EventLoopGroup tcpBossGroup;
    private EventLoopGroup tcpWorkGroup;
    private ServerBootstrap serverBootstrap;
    private Channel channel;
    private InetSocketAddress bindAddress;
    private boolean isRunning = false;

    @Override
    public void init(Map<String, Object> args) {
        this.tcpBossGroup = NettyEventLoopFactory.createEventLoopGroup(Const.DEFAULT_IO_THREADS, "fim-tcp-server-boss-");
        this.tcpWorkGroup = NettyEventLoopFactory.createEventLoopGroup(Const.DEFAULT_IO_THREADS, "fim-tcp-server-work-");
        this.serverBootstrap = new ServerBootstrap();
        this.bindAddress = new InetSocketAddress(Configs.getString(Configs.ConfigKey.SERVER_TCP_IP_KEY), Configs.getInt(Configs.ConfigKey.SERVER_TCP_PORT_KEY));
    }

    @Override
    public void start() {
        SessionHandler sessionHandler = SpiLoader.getInstance().getSpiByConfigOrPrimary(SessionHandler.class);
        this.bind(sessionHandler);
        this.channel.newSucceededFuture().addListener(future -> {
            if (future.isSuccess()) {
                this.isRunning = true;
                LOG.info("netty tcp server in {} port start finish....", Configs.getInt(Configs.ConfigKey.SERVER_TCP_PORT_KEY));
            }else {
                LOG.error("netty tcp server in {} port start fail....", Configs.getInt(Configs.ConfigKey.SERVER_TCP_PORT_KEY));
            }
        });
        this.channel.closeFuture().addListener(future -> this.stop());
    }

    @Override
    public void stop() {
        if (tcpBossGroup != null && !(tcpBossGroup.isShuttingDown() && tcpBossGroup.isShutdown() && tcpBossGroup.isTerminated())){
            tcpBossGroup.shutdownGracefully();
        }
        if (tcpWorkGroup != null && !(tcpWorkGroup.isShuttingDown() && tcpWorkGroup.isShutdown() && tcpWorkGroup.isTerminated())){
            tcpWorkGroup.shutdownGracefully();
        }
        this.isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void bind(SessionHandler sessionHandler) {
        initServerBootstrap(sessionHandler);
        ChannelFuture channelFuture = this.serverBootstrap
                .bind(this.bindAddress)
                .syncUninterruptibly();

        this.channel = channelFuture.channel();
    }

    public void initServerBootstrap(SessionHandler sessionHandler) {
        this.serverBootstrap
                .group(tcpBossGroup, tcpWorkGroup)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .channel(NettyEventLoopFactory.createServerSocketChannelClass())
                .childHandler(new TcpServerInitializer(sessionHandler));
    }
}
