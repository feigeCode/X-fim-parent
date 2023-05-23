package com.feige.fim.server.tcp;


import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Server;
import com.feige.api.session.SessionRepository;
import com.feige.fim.server.NettyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author feige
 */
public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SessionHandler sessionHandler;
    private final SessionRepository sessionRepository;

    public TcpServerInitializer(Server server) {
        this.sessionHandler = server.getSessionHandler();
        this.sessionRepository = server.getSessionRepository();
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new NettyServerHandler(sessionHandler, sessionRepository));
        pipeline.addLast(new IdleStateHandler(45,60,0,TimeUnit.SECONDS));

    }
}
