package com.feige.fim.server.ws;

import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Server;
import com.feige.api.session.SessionRepository;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


public class WsServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SessionHandler sessionHandler;
    private final SessionRepository sessionRepository;

    public WsServerInitializer(Server server, String wsPath) {
        this.sessionHandler = server.getSessionHandler();
        this.sessionRepository = server.getSessionRepository();
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        
    }
}
