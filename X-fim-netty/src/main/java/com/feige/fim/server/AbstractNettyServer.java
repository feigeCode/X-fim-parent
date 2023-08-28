package com.feige.fim.server;

import com.feige.api.codec.Codec;
import com.feige.api.constant.Const;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.AbstractServer;
import com.feige.api.sc.Listener;
import com.feige.api.sc.ServiceException;
import com.feige.api.session.SessionRepository;
import com.feige.fim.factory.NettyEventLoopFactory;
import com.feige.utils.logger.Loggers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;

import java.net.InetSocketAddress;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


public abstract class AbstractNettyServer extends AbstractServer {
    public static final Logger LOG = Loggers.SERVER;
    protected EventLoopGroup bossGroup;
    protected EventLoopGroup workGroup;
    protected ServerBootstrap serverBootstrap;
    protected Channel channel;

    public AbstractNettyServer(InetSocketAddress address, SessionHandler sessionHandler, SessionRepository sessionRepository, Codec codec) {
        super(address, sessionHandler, sessionRepository, codec);
    }

    @Override
    public void initialize() {
        if (!serverState.compareAndSet(ServerState.CREATED, ServerState.INITIALIZED)) {
            throw new ServiceException("Server already init");
        }
        this.bossGroup = createBossGroup();
        this.workGroup = createWorkerGroup();
        this.serverBootstrap = new ServerBootstrap();
    }

    @Override
    protected void doStart(Listener listener) {
        try {
            if (!serverState.compareAndSet(ServerState.INITIALIZED, ServerState.STARTING)) {
                throw new ServiceException("Server already started or have not init");
            }
            initServerBootstrap();
            ChannelFuture channelFuture = this.serverBootstrap
                    .bind(address)
                    .addListener(future -> {
                        if (future.isSuccess()) {
                            serverState.set(ServerState.STARTED);
                            LOG.info("netty [{}] server in {} port start finish....", getClass().getSimpleName() ,getAddress().getPort());
                            if (listener != null){
                                listener.onSuccess(address);
                            }
                        }else {
                            LOG.error("server start failure on:{}", getAddress().getPort(), future.cause());
                            if (listener != null) {
                                listener.onFailure(future.cause());
                            }
                        }
                    });
            this.channel = channelFuture.channel();
        }catch (Throwable throwable){
            LOG.error("server start exception", throwable);
            if (listener != null) {
                listener.onFailure(throwable);
            }
            throw new ServiceException("server start exception, port=" + getAddress().getPort(), throwable);
        }
    }

    @Override
    protected void doStop(Listener listener) {
        if (!serverState.compareAndSet(ServerState.STARTED, ServerState.SHUTDOWN)) {
            if (listener != null) {
                listener.onFailure(new ServiceException("server was already shutdown."));
            }
            LOG.error("{} was already shutdown.", this.getClass().getSimpleName());
            return;
        }
        LOG.info("try shutdown {}...", this.getClass().getSimpleName());
        try {
            if (channel != null) {
                // unbind.
                channel.close();
            }
        } catch (Throwable e) {
            LOG.warn(e.getMessage(), e);
        }
        try {
            if (this.serverBootstrap != null) {
                long timeout = timeoutMillis();
                long quietPeriod = Math.min(2000L, timeout);
                Future<?> bossGroupShutdownFuture = bossGroup.shutdownGracefully(quietPeriod, timeout, MILLISECONDS);
                Future<?> workerGroupShutdownFuture = workGroup.shutdownGracefully(quietPeriod, timeout, MILLISECONDS);
                bossGroupShutdownFuture.syncUninterruptibly();
                workerGroupShutdownFuture.syncUninterruptibly();
            }
        } catch (Throwable e) {
            LOG.warn(e.getMessage(), e);
        }
        LOG.info("{} shutdown success.", this.getClass().getSimpleName());
        if (listener != null) {
            listener.onSuccess(address);
        }
    }

    protected EventLoopGroup createBossGroup() {
        return NettyEventLoopFactory.createEventLoopGroup(getBossGroupThreadNum(), getBossGroupThreadName());
    }

    protected EventLoopGroup createWorkerGroup() {
        return NettyEventLoopFactory.createEventLoopGroup(getWorkerGroupThreadNum(), getWorkerGroupThreadName());
    }
    
    protected String getBossGroupThreadName(){
        return "fim-server-boss-";
    }
    
    protected String getWorkerGroupThreadName(){
        return "fim-server-work-";
    }

    protected int getBossGroupThreadNum(){
        return Const.DEFAULT_IO_THREADS;
    }

    protected int getWorkerGroupThreadNum(){
        return Const.DEFAULT_IO_THREADS;
    }
    
    protected void initServerBootstrap() {
        this.serverBootstrap
                .group(bossGroup, workGroup)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .channel(NettyEventLoopFactory.createServerSocketChannelClass());
    }
    
    
    
}
