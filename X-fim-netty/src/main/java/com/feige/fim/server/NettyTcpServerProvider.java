package com.feige.fim.server;

import com.feige.annotation.SpiComp;
import com.feige.annotation.Value;
import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Server;
import com.feige.api.session.SessionRepository;
import com.feige.framework.config.Configs;
import com.feige.fim.server.tcp.NettyTcpServer;


import java.net.InetSocketAddress;

@SpiComp("tcp")
public class NettyTcpServerProvider extends AbstractServerProvider {
    
    @Value(Configs.ConfigKey.SERVER_TCP_IP_KEY)
    private String ip;

    @Value(Configs.ConfigKey.SERVER_TCP_PORT_KEY)
    private int port = 8001;
    
    @Override
    public Server get() {
        InetSocketAddress socketAddress = getAddress(this.ip, this.port);
        SessionHandler sessionHandler = getSessionHandler();
        SessionRepository sessionRepository = getSessionRepository();
        Codec codec = getCodec();
        return new NettyTcpServer(socketAddress, sessionHandler, sessionRepository, codec);
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
