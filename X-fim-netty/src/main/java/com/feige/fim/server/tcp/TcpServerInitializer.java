package com.feige.fim.server.tcp;


import com.feige.api.sc.Server;
import com.feige.fim.adapter.NettyCodecAdapter;
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
    
    private final NettyCodecAdapter codec;
    private final NettyServerHandler serverHandler;

    public TcpServerInitializer(Server server) {
        this.serverHandler = new NettyServerHandler(server.getSessionHandler(), server.getSessionRepository());
        this.codec = new NettyCodecAdapter(server.getCodec(), server.getSessionRepository());
        
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(codec.getDecoder());
        pipeline.addLast(codec.getEncoder());
        pipeline.addLast(new IdleStateHandler(45,60,0,TimeUnit.SECONDS));
        pipeline.addLast(this.serverHandler);

    }

}
