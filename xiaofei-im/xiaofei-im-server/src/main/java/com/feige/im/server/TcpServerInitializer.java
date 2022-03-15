package com.feige.im.server;

import com.feige.im.codec.tcp.XiaoFeiTcpProtoBufDecoder;
import com.feige.im.codec.tcp.XiaoFeiTcpProtoBufEncoder;
import com.feige.im.handler.MsgListener;
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
public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final MsgListener listener;

    public TcpServerInitializer(MsgListener listener){
        this.listener = listener;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 自定义proto编解码器
        pipeline.addLast(new XiaoFeiTcpProtoBufDecoder());
        pipeline.addLast(new XiaoFeiTcpProtoBufEncoder());

        // 心跳
        pipeline.addLast(new IdleStateHandler(45,60,0,TimeUnit.SECONDS));
        pipeline.addLast(new ServerHeartbeatHandler());
        // 自定义的handler
        pipeline.addLast(new XiaoFeiImHandler(listener));
    }
}
