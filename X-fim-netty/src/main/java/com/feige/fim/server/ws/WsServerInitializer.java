package com.feige.fim.server.ws;

import com.feige.api.sc.Server;
import com.feige.fim.adapter.NettyCodecAdapter;
import com.feige.fim.server.NettyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;


public class WsServerInitializer extends ChannelInitializer<SocketChannel> {
    private final NettyCodecAdapter codec;
    private final NettyServerHandler serverHandler;

    public WsServerInitializer(Server server, String wsPath) {
        this.serverHandler = new NettyServerHandler(server.getSessionHandler(), server.getSessionRepository());
        this.codec = new NettyCodecAdapter(server.getCodec(), server.getSessionRepository());

    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(codec.getDecoder());
        pipeline.addLast(codec.getEncoder());
        pipeline.addLast(new IdleStateHandler(45,60,0, TimeUnit.SECONDS));
        pipeline.addLast(this.serverHandler);

    }
}
