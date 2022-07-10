package com.feige.im.client;

import com.feige.im.codec.tcp.XiaoFeiTcpMsgDecoder;
import com.feige.im.codec.tcp.XiaoFeiTcpMsgEncoder;
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
        pipeline.addLast(new XiaoFeiTcpMsgDecoder());
        pipeline.addLast(new XiaoFeiTcpMsgEncoder());

        // 自定义的handler
        pipeline.addLast(new XiaoFeiImHandler(listener));
    }
}
