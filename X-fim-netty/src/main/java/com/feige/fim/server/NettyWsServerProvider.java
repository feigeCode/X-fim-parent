package com.feige.fim.server;

import com.feige.fim.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Server;
import com.feige.api.session.SessionRepository;
import com.feige.fim.codec.PacketCodec;
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

    @Override
    protected Codec getCodec() {
        Integer maxPacketSize = Configs.getInt(Configs.ConfigKey.CODEC_MAX_PACKET_SIZE_KEY);
        Integer heartbeat = Configs.getInt(Configs.ConfigKey.CODEC_HEARTBEAT_KEY);
        Integer version = Configs.getInt(Configs.ConfigKey.CODEC_VERSION_KEY);
        Integer headerLength = Configs.getInt(Configs.ConfigKey.CODEC_HEADER_LENGTH_KEY);
        String checkSumKey = Configs.getString(Configs.ConfigKey.CODEC_CHECK_SUM_KEY);
        return new PacketCodec(maxPacketSize, heartbeat.byteValue(), version.byteValue(), headerLength, checkSumKey);
    }
}
