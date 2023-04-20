package com.feige.fim.server.tcp;

import com.feige.api.base.Listener;
import com.feige.api.constant.Const;
import com.feige.fim.factory.NettyEventLoopFactory;
import com.feige.log.Logger;
import com.feige.api.sc.IServer;
import com.feige.api.config.Configs;
import com.feige.log.Loggers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;

import java.util.Map;

public class NettyTcpServer implements IServer {

    public static final Logger LOG = Loggers.SERVER;

    private EventLoopGroup tcpBossGroup;
    private EventLoopGroup tcpWorkGroup;
    private Class<? extends ServerChannel> tcpServerChannel;
    private boolean isRunning = false;

    @Override
    public void init(Map<String, Object> args) {
        this.tcpBossGroup = NettyEventLoopFactory.createEventLoopGroup(Const.DEFAULT_IO_THREADS, "tcp-server-boss-");
        this.tcpWorkGroup = NettyEventLoopFactory.createEventLoopGroup(Const.DEFAULT_IO_THREADS, "tcp-server-work-");
        this.tcpServerChannel = NettyEventLoopFactory.createServerSocketChannelClass();
    }

    @Override
    public void start() {
        createTcpServer();
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
    public void bind(Listener listener) {

    }


    /**
     * @description: 创建Tcp启动器
     * @author: feige
     * @date: 2021/11/14 16:19
     * @return: void
     */
    public void createTcpServer() {
        ChannelFuture channelFuture = new ServerBootstrap()
                .group(tcpBossGroup, tcpWorkGroup)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .localAddress(Configs.Server.TCP_PORT)
                .channel(tcpServerChannel)
                .childHandler(new TcpServerInitializer())
                .bind().syncUninterruptibly();
        channelFuture.channel().newSucceededFuture().addListener(future -> {
            if (future.isSuccess()) {
                this.isRunning = true;
                LOG.info("netty tcp server in {} port start finish....", Configs.Server.TCP_PORT);
            }else {
                LOG.error("netty tcp server in {} port start fail....", Configs.Server.TCP_PORT);
            }
        });
        channelFuture.channel().closeFuture().addListener(future -> this.stop());

    }
}
