package com.feige.fim.factory;

import com.feige.api.session.Session;
import com.feige.api.session.SessionRepository;
import com.feige.fim.session.NettySession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

public class NettySessionFactory {

    public static Session getOrAddSession(ChannelHandlerContext ctx, SessionRepository sessionRepository){
        final Channel channel = ctx.channel();
        return sessionRepository.computeIfAbsent(channel.id().asShortText(), k -> {
            NettySession nettySession = new NettySession(channel);
            channel.closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    future.removeListener(this);
                    sessionRepository.removeAndClose(nettySession);
                }
            });
            return nettySession;
        });
    }
}
