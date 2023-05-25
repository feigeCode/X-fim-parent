package com.feige.fim.server;

import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Server;
import com.feige.api.session.SessionRepository;
import com.feige.fim.config.Configs;
import com.feige.fim.server.ws.NettyWsServer;
import com.feige.fim.utils.StringUtil;

import java.net.InetSocketAddress;

public class NettyWsServerProvider extends AbstractServerProvider {

    @Override
    protected InetSocketAddress getAddress() {
        String ip = Configs.getString(Configs.ConfigKey.SERVER_WS_IP_KEY);
        Integer port = Configs.getInt(Configs.ConfigKey.SERVER_WS_PORT_KEY, 8002);
        InetSocketAddress socketAddress;
        if (StringUtil.isNotBlank(ip)){
            socketAddress = new InetSocketAddress(ip, port);
        }else {
            socketAddress = new InetSocketAddress(port);
        }
        return socketAddress;
    }

    @Override
    public Server get() {
        InetSocketAddress socketAddress = getAddress();
        SessionHandler sessionHandler = getSessionHandler();
        SessionRepository sessionRepository = getSessionRepository();
        Codec codec = getCodec();
        return new NettyWsServer(socketAddress, sessionHandler, sessionRepository, codec);
    }

    @Override
    public String getKey() {
        return "ws";
    }
}
