package com.feige.fim.factory;

import com.feig.utils.NameThreadFactory;
import com.feig.utils.OsUtil;
import com.feige.api.config.Configs;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ThreadFactory;

public class NettyEventLoopFactory {

    public static EventLoopGroup createEventLoopGroup(int threads, String threadFactoryName) {
        ThreadFactory threadFactory = new NameThreadFactory(threadFactoryName);
        return enableEpoll() ? new EpollEventLoopGroup(threads, threadFactory) :
                new NioEventLoopGroup(threads, threadFactory);
    }

    public static Class<? extends SocketChannel> createSocketChannelClass() {
        return enableEpoll() ? EpollSocketChannel.class : NioSocketChannel.class;
    }

    public static Class<? extends ServerSocketChannel> createServerSocketChannelClass() {
        return enableEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }

    private static boolean enableEpoll() {
        if (Configs.Server.ENABLE_EPOLL && Epoll.isAvailable()) {
            return OsUtil.isLinux();
        }
        return false;
    }
}
