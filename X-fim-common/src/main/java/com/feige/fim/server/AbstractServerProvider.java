package com.feige.fim.server;

import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.ServerProvider;
import com.feige.api.session.SessionRepository;
import com.feige.fim.config.Configs;
import com.feige.fim.spi.SpiLoaderUtils;
import com.feige.fim.utils.StringUtil;

import java.net.InetSocketAddress;

public abstract class AbstractServerProvider implements ServerProvider {



    /**
     * get session handler.
     *
     * @return session handler
     */
    protected SessionHandler getSessionHandler() {
        return SpiLoaderUtils.getByConfig(SessionHandler.class);
    }

    /**
     * get address.
     *
     * @return address.
     */
    protected InetSocketAddress getAddress() {
        String ip = Configs.getString(Configs.ConfigKey.SERVER_TCP_IP_KEY);
        Integer port = Configs.getInt(Configs.ConfigKey.SERVER_TCP_PORT_KEY, 8001);
        InetSocketAddress socketAddress;
        if (StringUtil.isNotBlank(ip)){
            socketAddress = new InetSocketAddress(ip, port);
        }else {
            socketAddress = new InetSocketAddress(port);
        }
        return socketAddress;
    }


    /**
     * get codec
     * @return codec
     */
    protected Codec getCodec() {
        return SpiLoaderUtils.getByConfig(Codec.class);
    }

    /**
     * get session repository
     * @return session repository
     */
    protected SessionRepository getSessionRepository() {
        return SpiLoaderUtils.getByConfig(SessionRepository.class);
    }
}
