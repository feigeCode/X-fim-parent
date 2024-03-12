package com.feige.fim.adapter;

import com.feige.api.handler.SessionHandler;
import com.feige.api.session.Session;
import com.feige.fim.event.NettyEventTrigger;
import com.feige.fim.factory.NettySessionFactory;
import com.feige.utils.event.EventDispatcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;

@ChannelHandler.Sharable
public class NettyChannelHandlerAdapter extends ChannelInboundHandlerAdapter {

    private final SessionHandler sessionHandler;
    public NettyChannelHandlerAdapter(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpResponse){
            super.channelRead(ctx, msg);
            return;
        }
        sessionHandler.received(toSession(ctx), msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sessionHandler.connected(toSession(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        sessionHandler.disconnected(toSession(ctx));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        sessionHandler.caught(toSession(ctx), cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        EventDispatcher.fire(new NettyEventTrigger(toSession(ctx), evt));
        super.userEventTriggered(ctx, evt);
    }

    protected Session toSession(ChannelHandlerContext ctx){
        return NettySessionFactory.getOrAddSession(ctx);
    }
}
