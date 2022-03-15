package com.feige.im.server;

import com.feige.im.handler.MsgListener;
import com.feige.im.handler.XiaoFeiImHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;

/**
 * @author feige<br />
 * @ClassName: UdpServerInitializer <br/>
 * @Description: <br/>
 * @date: 2022/3/14 16:48<br/>
 */
public class UdpServerInitializer extends ChannelInitializer<DatagramChannel> {

    private final MsgListener listener;

    public UdpServerInitializer(MsgListener listener){
        this.listener = listener;
    }


    @Override
    protected void initChannel(DatagramChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 自定义的handler
        pipeline.addLast(new XiaoFeiImHandler(listener));
    }
}
