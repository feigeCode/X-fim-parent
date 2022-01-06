package com.feige.im.server;

import com.feige.im.codec.XiaoFeiProtoBufDecoder;
import com.feige.im.codec.XiaoFeiProtoBufEncoder;
import com.feige.im.handler.MsgProcessor;
import com.feige.im.handler.ServerHeartbeatHandler;
import com.feige.im.handler.XiaoFeiImHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author feige
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private final MsgProcessor processor;

    public NettyServerInitializer(MsgProcessor msgProcessor){
        this.processor = msgProcessor;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 自定义proto编解码器
        pipeline.addLast(new XiaoFeiProtoBufDecoder());
        pipeline.addLast(new XiaoFeiProtoBufEncoder());
        // 心跳
        pipeline.addLast(new IdleStateHandler(45,60,0,TimeUnit.SECONDS));
        pipeline.addLast(new ServerHeartbeatHandler());
        // 自定义的handler
        pipeline.addLast(new XiaoFeiImHandler(processor));
    }
}
