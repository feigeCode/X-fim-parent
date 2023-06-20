package com.feige.fim.netty;

import com.feige.fim.event.ChannelActive;
import com.feige.fim.listener.ChannelActiveListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientHandler extends SimpleChannelInboundHandler<Object> {
    private final NettyClient nettyClient;

    public NettyClientHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        nettyClient.getMsgListener().onReceivedMsg(o);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ChannelActive channelActive = new ChannelActive(nettyClient, ChannelActive.CHANNEL_ACTIVE);
        ChannelActiveListener.getInstance().handleEvent(channelActive);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ChannelActive channelActive = new ChannelActive(nettyClient, ChannelActive.CHANNEL_INACTIVE);
        ChannelActiveListener.getInstance().handleEvent(channelActive);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ChannelActive channelActive = new ChannelActive(nettyClient, ChannelActive.CHANNEL_INACTIVE, cause);
        ChannelActiveListener.getInstance().handleEvent(channelActive);
    }
}
