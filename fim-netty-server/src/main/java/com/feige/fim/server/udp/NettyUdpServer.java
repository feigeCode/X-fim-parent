package com.feige.fim.server.udp;

import com.feige.fim.constant.Const;
import com.feige.api.handler.SessionHandler;
import com.feige.fim.sc.AbstractServer;
import com.feige.api.sc.Listener;
import com.feige.api.sc.ServiceException;
import com.feige.api.session.SessionRepository;
import com.feige.api.codec.Codec;
import com.feige.fim.factory.NettyEventLoopFactory;
import com.feige.utils.logger.Loggers;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;

import java.net.InetSocketAddress;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class NettyUdpServer extends AbstractServer {

    public static final Logger LOG = Loggers.SERVER;
    protected EventLoopGroup group;
    protected Bootstrap bootstrap;
    protected Channel channel;
    
    
    public NettyUdpServer(InetSocketAddress address, SessionHandler sessionHandler, SessionRepository sessionRepository, Codec codec) {
        super(address, sessionHandler, sessionRepository, codec);
    }


    @Override
    public void initialize() {
        this.group = createGroup();
        this.bootstrap = new Bootstrap();
    }

    @Override
    protected void doStart(Listener listener) {
        try {
            if (!serverState.compareAndSet(ServerState.INITIALIZED, ServerState.STARTING)) {
                throw new ServiceException("Server already started or have not init");
            }
            initBootstrap();
            ChannelFuture channelFuture = this.bootstrap
                    .bind(address)
                    .addListener(future -> {
                        if (future.isSuccess()) {
                            serverState.set(ServerState.STARTED);
                            LOG.info("netty tcp server in {} port start finish....", getAddress().getPort());
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
            if (this.bootstrap != null) {
                long timeout = timeoutMillis();
                long quietPeriod = Math.min(2000L, timeout);
                Future<?> bossGroupShutdownFuture = group.shutdownGracefully(quietPeriod, timeout, MILLISECONDS);
                bossGroupShutdownFuture.syncUninterruptibly();
            }
        } catch (Throwable e) {
            LOG.warn(e.getMessage(), e);
        }
        LOG.info("{} shutdown success.", this.getClass().getSimpleName());
        if (listener != null) {
            listener.onSuccess(address);
        }
    }

    protected EventLoopGroup createGroup() {
        return NettyEventLoopFactory.createEventLoopGroup(getGroupThreadNum(), getGroupThreadName());
    }

    protected String getGroupThreadName(){
        return "fim-udp-server-boss-";
    }
    

    protected int getGroupThreadNum(){
        return Const.DEFAULT_IO_THREADS;
    }


    protected void initBootstrap() {
        this.bootstrap
                .group(group)
                .channel(NettyEventLoopFactory.createDatagramChannelClass())
                .handler(new UdpServerInitializer(this))
                // 支持广播
                .option(ChannelOption.SO_BROADCAST, true)
                // 设置读缓冲区
                .option(ChannelOption.SO_RCVBUF, 1024 * 1024)
                // 设置写缓冲区
                .option(ChannelOption.SO_SNDBUF, 1024 * 1024);
    }
}
