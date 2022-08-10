package com.feige.fim.server.tcp;

import com.feig.utils.NameThreadFactory;
import com.feig.utils.OsUtil;
import com.feige.api.base.Listener;
import com.feige.log.Logger;
import com.feige.api.sc.IServer;
import com.feige.api.config.Configs;
import com.feige.log.Loggers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Map;

public class NettyTcpServer implements IServer {

    public static final Logger LOG = Loggers.SERVER;

    private EventLoopGroup tcpBossGroup;
    private EventLoopGroup tcpWorkGroup;
    private Class<? extends ServerChannel> tcpServerChannel;

    @Override
    public void init(Map<String, Object> args) {
        if (OsUtil.isLinux() && Configs.Server.ENABLE_EPOLL){
            this.tcpBossGroup = new EpollEventLoopGroup(new NameThreadFactory("tcp-server-epoll-boss-"));
            this.tcpWorkGroup = new EpollEventLoopGroup(new NameThreadFactory("tcp-server-epoll-work-"));
            this.tcpServerChannel = EpollServerSocketChannel.class;
        }else {
            this.tcpBossGroup = new NioEventLoopGroup(new NameThreadFactory("tcp-server-nio-boss-"));
            this.tcpWorkGroup = new NioEventLoopGroup(new NameThreadFactory("tcp-server-nio-work-"));
            this.tcpServerChannel = NioServerSocketChannel.class;
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        if (tcpBossGroup != null && !(tcpBossGroup.isShuttingDown() && tcpBossGroup.isShutdown() && tcpBossGroup.isTerminated())){
            tcpBossGroup.shutdownGracefully();
        }
        if (tcpWorkGroup != null && !(tcpWorkGroup.isShuttingDown() && tcpWorkGroup.isShutdown() && tcpWorkGroup.isTerminated())){
            tcpWorkGroup.shutdownGracefully();
        }
    }

    @Override
    public boolean isRunning() {
        return false;
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
                LOG.info("netty tcp server in {} port start finish....", Configs.Server.TCP_PORT);
            }else {
                LOG.error("netty tcp server in {} port start fail....", Configs.Server.TCP_PORT);
            }
        });
        channelFuture.channel().closeFuture().addListener(future -> this.stop());

    }
}
