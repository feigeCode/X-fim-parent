package com.feige.fim.adapter;

import com.feige.fim.session.AbstractSessionRepository;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

public class NettySessionRepository extends AbstractSessionRepository {

    /**
     * 监听连接关闭
     */
    public transient final ChannelFutureListener remover = future -> {
        future.removeListener(NettySessionRepository.this.remover);
        final Channel channel = future.channel();
        removeAndClose(get(channel.id().asShortText()));
    };

    @Override
    public String getKey() {
        return "nettySessionRepository";
    }

    @Override
    public boolean primary() {
        return true;
    }
}
