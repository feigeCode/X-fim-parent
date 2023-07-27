package com.feige.fim.netty;

import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.framework.annotation.Value;
import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Client;
import com.feige.framework.api.spi.InstanceProvider;
import com.feige.fim.config.ClientConfigKey;
import com.feige.fim.utils.StringUtils;
import com.google.auto.service.AutoService;

import java.net.InetSocketAddress;

@SpiComp("nettyClient")
@AutoService(InstanceProvider.class)
public class NettyClientInstanceProvider implements InstanceProvider<Client> {
    @Value(ClientConfigKey.CLIENT_TCP_IP_KEY)
    private String ip;

    @Value(ClientConfigKey.CLIENT_TCP_PORT_KEY)
    private int port = 8001;

    @Inject
    private SessionHandler sessionHandler;

    @Inject
    private Codec codec;
    
    @Override
    public Client getInstance() {
        return new NettyClient(getAddress(this.ip, this.port), getCodec(), getSessionHandler());
    }

    @Override
    public Class<Client> getType() {
        return Client.class;
    }

    

    /**
     * get session handler.
     *
     * @return session handler
     */
    protected SessionHandler getSessionHandler() {
        return sessionHandler;
    }

    public void setSessionHandler(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    /**
     * get address.
     *
     * @return address.
     */
    protected InetSocketAddress getAddress(String ip, int port) {
        InetSocketAddress socketAddress;
        if (StringUtils.isNotBlank(ip)){
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
    protected  Codec getCodec() {
        return this.codec;
    }

    public void setCodec(Codec codec) {
        this.codec = codec;
    }
    
}
