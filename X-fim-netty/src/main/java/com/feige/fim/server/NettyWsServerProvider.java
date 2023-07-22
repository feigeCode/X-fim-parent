package com.feige.fim.server;

import com.feige.api.annotation.SpiComp;
import com.feige.api.annotation.Value;
import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Server;
import com.feige.api.session.SessionRepository;
import com.feige.fim.config.Configs;
import com.feige.fim.server.ws.NettyWsServer;

import java.net.InetSocketAddress;

@SpiComp("ws")
public class NettyWsServerProvider extends AbstractServerProvider {

    @Value(Configs.ConfigKey.SERVER_WS_IP_KEY)
    private String ip;

    @Value(Configs.ConfigKey.SERVER_WS_PORT_KEY)
    private int port = 8002;


    @Override
    public Server get() {
        InetSocketAddress socketAddress = getAddress(this.ip, this.port);
        SessionHandler sessionHandler = getSessionHandler();
        SessionRepository sessionRepository = getSessionRepository();
        Codec<?> codec = getCodec();
        return new NettyWsServer(socketAddress, sessionHandler, sessionRepository, codec);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
