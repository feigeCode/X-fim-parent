package com.feige.fim.server.tcp;

import com.feige.api.constant.Const;
import com.feige.api.sc.AbstractServer;
import com.feige.api.sc.Listener;
import com.feige.api.handler.RemotingException;
import com.feige.api.handler.SessionHandler;
import com.feige.fim.config.Configs;
import com.feige.fim.factory.NettyEventLoopFactory;
import com.feige.fim.lg.Loggers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

public class NettyTcpAbstractServer extends AbstractServer {

    public static final Logger LOG = Loggers.SERVER;

    private EventLoopGroup tcpBossGroup;
    private EventLoopGroup tcpWorkGroup;
    private ServerBootstrap serverBootstrap;
    private Channel channel;
    private InetSocketAddress bindAddress;
    private boolean isRunning = false;

    public NettyTcpAbstractServer(SessionHandler handler) {
        super(handler);
    }


    public void initialize() throws IllegalStateException {
        this.tcpBossGroup = NettyEventLoopFactory.createEventLoopGroup(Const.DEFAULT_IO_THREADS, "fim-tcp-server-boss-");
        this.tcpWorkGroup = NettyEventLoopFactory.createEventLoopGroup(Const.DEFAULT_IO_THREADS, "fim-tcp-server-work-");
        this.serverBootstrap = new ServerBootstrap();
        this.bindAddress = new InetSocketAddress(Configs.getString(Configs.ConfigKey.SERVER_TCP_IP_KEY), Configs.getInt(Configs.ConfigKey.SERVER_TCP_PORT_KEY));
    }

    @Override
    public void start(Listener listener) {
        
    }

    @Override
    public void stop(Listener listener) {

    }

    public CompletableFuture<Boolean> start() {
        this.channel.newSucceededFuture().addListener(future -> {
            if (future.isSuccess()) {
                this.isRunning = true;
                LOG.info("netty tcp server in {} port start finish....", Configs.getInt(Configs.ConfigKey.SERVER_TCP_PORT_KEY));
            }else {
                LOG.error("netty tcp server in {} port start fail....", Configs.getInt(Configs.ConfigKey.SERVER_TCP_PORT_KEY));
            }
        });
        this.channel.closeFuture().addListener(future -> this.stop());
        return null;
    }

    public void destroy() throws IllegalStateException {

    }

    public CompletableFuture<Boolean> stop() {
        if (tcpBossGroup != null && !(tcpBossGroup.isShuttingDown() && tcpBossGroup.isShutdown() && tcpBossGroup.isTerminated())){
            tcpBossGroup.shutdownGracefully();
        }
        if (tcpWorkGroup != null && !(tcpWorkGroup.isShuttingDown() && tcpWorkGroup.isShutdown() && tcpWorkGroup.isTerminated())){
            tcpWorkGroup.shutdownGracefully();
        }
        this.isRunning = false;
        return null;
    }

    @Override
    public SessionHandler getSessionHandler() {
        return null;
    }

    @Override
    public boolean isBound() {
        return isRunning;
    }

    @Override
    public void bind(InetSocketAddress bindAddress) {
        initServerBootstrap();
        ChannelFuture channelFuture = this.serverBootstrap
                .bind(bindAddress)
                .syncUninterruptibly();
        this.channel = channelFuture.channel();
    }

    public void initServerBootstrap() {
        this.serverBootstrap
                .group(tcpBossGroup, tcpWorkGroup)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .channel(NettyEventLoopFactory.createServerSocketChannelClass())
                .childHandler(new TcpServerInitializer(getSessionHandler()));
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void send(Object message, boolean sent) throws RemotingException {

    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    protected void doOpen() throws Throwable {
        
    }

    @Override
    protected void doClose() throws Throwable {

    }

    @Override
    protected int getSessionsSize() {
        return 0;
    }
}
