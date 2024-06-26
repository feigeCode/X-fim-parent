package com.feige.fim.server;

import com.feige.fim.sc.AbstractServerProvider;
import com.feige.utils.spi.annotation.SPI;
import com.feige.framework.annotation.Value;
import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.fim.sc.Server;
import com.feige.fim.sc.ServerProvider;
import com.feige.api.session.SessionRepository;
import com.feige.fim.constant.ServerConfigKey;
import com.feige.fim.server.tcp.NettyTcpServer;



import java.net.InetSocketAddress;

@SPI(value="tcp", interfaces = ServerProvider.class)
public class NettyTcpServerProvider extends AbstractServerProvider {
    
    @Value(ServerConfigKey.SERVER_TCP_IP_KEY)
    private String ip;

    @Value(ServerConfigKey.SERVER_TCP_PORT_KEY)
    private int port = 8001;
    
    @Override
    public Server get() {
        InetSocketAddress socketAddress = getAddress(this.ip, this.port);
        SessionHandler sessionHandler = getSessionHandler();
        SessionRepository sessionRepository = getSessionRepository();
        Codec codec = getCodec();
        return new NettyTcpServer(socketAddress, sessionHandler, sessionRepository, codec);
    }
    

}
