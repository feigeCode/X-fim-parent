package com.feige.im.client;

import com.feige.im.codec.tcp.XiaoFeiTcpProtoBufDecoder;
import com.feige.im.codec.tcp.XiaoFeiTcpProtoBufEncoder;
import com.feige.im.handler.MsgListener;
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
    private final MsgListener listener;

    public NettyClientInitializer(MsgListener listener) {
        this.listener = listener;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 自定义proto编解码器
        pipeline.addLast(new XiaoFeiTcpProtoBufDecoder());
        pipeline.addLast(new XiaoFeiTcpProtoBufEncoder());

        // 自定义的handler
        pipeline.addLast(new XiaoFeiImHandler(listener));
    }
}
