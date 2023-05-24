package com.feige.fim.adapter;

import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.api.session.SessionRepository;
import com.feige.fim.session.AbstractSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;


public class NettyChannel extends AbstractSession {
    
 
    private final Channel channel;
    
    public NettyChannel(Channel channel) {
        this.channel = channel;
        this.markActive(this.channel.isActive());
    }

    public static Session getOrAddSession(ChannelHandlerContext ctx, SessionRepository sessionRepository){
        final Channel channel = ctx.channel();
        return sessionRepository.computeIfAbsent(channel.id().asShortText(), k -> {
            if (sessionRepository instanceof NettySessionRepository){
                channel.closeFuture().addListener(((NettySessionRepository) sessionRepository).remover);
            }
            return new NettyChannel(channel);
        });
    }

    @Override
    public String getId() {
        return channel.id().asLongText();
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
    public boolean isConnected() {
        return super.isConnected() && channel.isActive() && channel.isWritable();
    }

    @Override
    public String getKey() {
        return "session";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NettyChannel that = (NettyChannel) o;

        return channel.id().equals(that.channel.id());
    }

    @Override
    public int hashCode() {
        return channel.id().hashCode();
    }
    
}
