package com.feige.fim.netty;

import com.feige.fim.session.NettySession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientHandler extends SimpleChannelInboundHandler<Object> {
    private final NettyClient nettyClient;

    public NettyClientHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        nettyClient.getSessionHandler().received(nettyClient.getSession(), o);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        nettyClient.sessionActive(new NettySession(ctx.channel()));
        nettyClient.getSessionHandler().connected(nettyClient.getSession());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        nettyClient.getSessionHandler().disconnected(nettyClient.getSession());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        nettyClient.getSessionHandler().caught(nettyClient.getSession(), cause);
    }
}
