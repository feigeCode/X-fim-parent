package com.feige.fim.adapter;

import com.feige.api.session.ISession;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

public class NettyChannelAdapter implements ISession {

    private final ChannelHandlerContext ctx;

    public NettyChannelAdapter(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public static ISession fromCtx(ChannelHandlerContext ctx){
        return new NettyChannelAdapter(ctx);
    }

    @Override
    public String getId() {
        return ctx.channel().id().asLongText();
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) ctx.channel().localAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) ctx.channel().remoteAddress();
    }

    @Override
    public void write(Object msg) {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean hasAttr(String key) {
        return false;
    }

    @Override
    public Object getAttr(String key) {
        return null;
    }

    @Override
    public void setAttr(String key, Object value) {

    }

    @Override
    public void removeAttr(String key) {

    }

    @Override
    public String getKey() {
        return null;
    }
}
