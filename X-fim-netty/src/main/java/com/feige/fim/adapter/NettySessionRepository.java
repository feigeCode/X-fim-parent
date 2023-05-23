package com.feige.fim.adapter;

import com.feige.api.session.Session;
import com.feige.fim.session.AbstractSessionRepository;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class NettySessionRepository extends AbstractSessionRepository {
    
    public Session getOrAdd(ChannelHandlerContext ctx){
        final Channel channel = ctx.channel();
        final String id = channel.id().asShortText();
        return sessionMap.get(id);
    }
}
