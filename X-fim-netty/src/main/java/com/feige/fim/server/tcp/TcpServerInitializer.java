package com.feige.fim.server.tcp;


import com.feige.api.handler.SessionHandler;
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

    public TcpServerInitializer(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        


        // 心跳
        pipeline.addLast(new IdleStateHandler(45,60,0,TimeUnit.SECONDS));

    }
}
