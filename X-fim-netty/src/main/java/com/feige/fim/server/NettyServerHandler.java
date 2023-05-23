package com.feige.fim.server;

import com.feige.api.handler.SessionHandler;
import com.feige.api.session.Session;
import com.feige.api.session.SessionRepository;
import com.feige.fim.adapter.NettyChannel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author feige<br />
 * @ClassName: NettyServerHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/21 14:07<br/>
 */
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final SessionHandler sessionHandler;
    private final SessionRepository sessionRepository;

    public NettyServerHandler(SessionHandler sessionHandler, SessionRepository sessionRepository) {
        this.sessionHandler = sessionHandler;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
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
    
    protected Session toSession(ChannelHandlerContext ctx){
        return NettyChannel.fromChannel(ctx, sessionRepository);
    }
}
