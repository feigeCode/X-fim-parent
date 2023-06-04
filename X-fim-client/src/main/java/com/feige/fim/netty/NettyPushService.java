package com.feige.fim.netty;

import com.feige.fim.api.PushService;
import io.netty.channel.Channel;

/**
 * @author feige<br />
 * @ClassName: NettyPushService <br/>
 * @Description: <br/>
 * @date: 2023/6/4 16:03<br/>
 */
public class NettyPushService implements PushService {
    
    private final Channel channel;
    
    @Override
    public boolean push(Object msg) {
        channel.writeAndFlush(msg);
        return true;
    }

    public NettyPushService(Channel channel) {
        this.channel = channel;
    }
}
