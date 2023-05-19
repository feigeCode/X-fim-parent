package com.feige.fim.server.ws;

import com.feige.api.handler.SessionHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


public class WsServerInitializer extends ChannelInitializer<SocketChannel> {
    
    public WsServerInitializer(SessionHandler sessionHandler, String wsPath) {
        
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        
    }
}
