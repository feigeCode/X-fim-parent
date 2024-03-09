package com.feige.fim.factory;

import com.feige.api.session.Session;
import com.feige.api.session.SessionRepository;
import com.feige.fim.session.NettySession;
import com.feige.framework.utils.AppContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

public class NettySessionFactory {

    private static SessionRepository sessionRepository;


    public static SessionRepository getSessionRepository() {
        if (sessionRepository == null) {
            sessionRepository = AppContext.get(SessionRepository.class);
        }
        return sessionRepository;
    }

    public static Session createSession(ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        NettySession nettySession = new NettySession(channel);
        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                getSessionRepository().removeAndClose(nettySession);
                future.removeListener(this);
            }
        });
        return nettySession;
    }

    public static Session getOrAddSession(ChannelHandlerContext ctx){
        final Channel channel = ctx.channel();
        String id = channel.id().asShortText();
        return getSessionRepository().computeIfAbsent(id, k -> createSession(ctx));
    }
}
