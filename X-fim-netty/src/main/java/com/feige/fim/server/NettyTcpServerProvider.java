package com.feige.fim.server;

import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Server;
import com.feige.api.session.SessionRepository;
import com.feige.fim.codec.PacketCodec;
import com.feige.fim.config.Configs;
import com.feige.fim.server.tcp.NettyTcpServer;


import java.net.InetSocketAddress;

public class NettyTcpServerProvider extends AbstractServerProvider {
    
    @Override
    public Server get() {
        InetSocketAddress socketAddress = getAddress();
        SessionHandler sessionHandler = getSessionHandler();
        SessionRepository sessionRepository = getSessionRepository();
        Codec codec = getCodec();
        return new NettyTcpServer(socketAddress, sessionHandler, sessionRepository, codec);
    }

    @Override
    public String getKey() {
        return "tcp";
    }

    @Override
    protected Codec getCodec(){
        Integer maxPacketSize = Configs.getInt(Configs.ConfigKey.CODEC_MAX_PACKET_SIZE_KEY);
        Integer heartbeat = Configs.getInt(Configs.ConfigKey.CODEC_HEARTBEAT_KEY);
        Integer version = Configs.getInt(Configs.ConfigKey.CODEC_VERSION_KEY);
        Integer headerLength = Configs.getInt(Configs.ConfigKey.CODEC_HEADER_LENGTH_KEY);
        String  checkSumKey = Configs.getString(Configs.ConfigKey.CODEC_CHECK_SUM_KEY);
        return new PacketCodec(maxPacketSize, heartbeat.byteValue(), version.byteValue(), headerLength, checkSumKey);
    }
}
