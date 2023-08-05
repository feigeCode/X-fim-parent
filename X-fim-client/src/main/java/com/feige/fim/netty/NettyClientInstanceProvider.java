package com.feige.fim.netty;

import com.feige.fim.config.ClientConfig;
import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Client;
import com.feige.framework.api.spi.InstanceProvider;
import com.google.auto.service.AutoService;

@SpiComp("nettyClient")
@AutoService(InstanceProvider.class)
public class NettyClientInstanceProvider implements InstanceProvider<Client> {
    
    @Inject
    private ClientConfig clientConfig;
    
    @Inject
    private SessionHandler sessionHandler;

    @Inject
    private Codec codec;
    
    @Override
    public Client getInstance() {
        return new NettyClient(clientConfig, codec, sessionHandler);
    }

    @Override
    public Class<Client> getType() {
        return Client.class;
    }
    
    
}
