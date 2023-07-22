package com.feige.fim.session;

import com.feige.api.handler.RemotingException;
import com.feige.api.session.AbstractSession;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;


public class NettySession extends AbstractSession {
    
 
    private final Channel channel;
    
    public NettySession(Channel channel) {
        this.channel = channel;
        this.markActive(this.channel.isActive());
    }
    
    @Override
    public String getId() {
        return channel.id().asShortText();
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NettySession that = (NettySession) o;

        return channel.id().equals(that.channel.id());
    }

    @Override
    public int hashCode() {
        return channel.id().hashCode();
    }
    
}
