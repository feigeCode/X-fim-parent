package com.feige.im.client;

import com.feige.im.codec.XiaoFeiProtoBufDecoder;
import com.feige.im.codec.XiaoFeiProtoBufEncoder;
import com.feige.im.handler.MsgProcessor;
import com.feige.im.handler.XiaoFeiImHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author feige<br />
 * @ClassName: NettyClientInitializer <br/>
 * @Description: <br/>
 * @date: 2021/11/7 18:34<br/>
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    private final MsgProcessor processor;
    private final String ip;
    private final int port;

    public NettyClientInitializer(MsgProcessor processor, String ip, int port) {
        this.processor = processor;
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 自定义proto编解码器
        pipeline.addLast(new XiaoFeiProtoBufDecoder());
        pipeline.addLast(new XiaoFeiProtoBufEncoder());

        // 自定义的handler
        pipeline.addLast(new XiaoFeiImHandler(processor));
    }
}
