package com.feige.fim.adapter;

import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.api.session.SessionRepository;
import com.feige.fim.session.AbstractSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;


public class NettyChannelAdapter extends AbstractSession {
 
    private final Channel channel;
    
    public NettyChannelAdapter(Channel channel) {
        this.channel = channel;
        this.markActive(this.channel.isActive());
    }

    public static Session fromChannel(ChannelHandlerContext ctx, SessionRepository sessionRepository){
        final Channel channel = ctx.channel();
        final Session session = ((NettySessionRepository) sessionRepository).getOrAdd(ctx);
        if (session == null){
            return new NettyChannelAdapter(channel);
        }
        return session;
    }

    @Override
    public String getId() {
        return channel.id().asLongText();
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) channel.localAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    @Override
    public void write(Object msg) throws RemotingException {
        if (isClosed()) {
            throw new RemotingException(this, "Failed to write message " + 
                    (msg == null ? "" : msg.getClass().getName()) + 
                    ", cause: Channel closed. channel: " + getLocalAddress() + " -> " + getRemoteAddress());
        }
        channel.writeAndFlush(msg);
    }

    @Override
    public void close() {
        super.close();
        channel.close();
    }

    @Override
    public String getKey() {
        return "session";
    }
    
}
