package com.feige.fim.netty;

import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Client;
import com.feige.framework.api.spi.InstanceProvider;


@SpiComp(value="nettyClient", interfaces = InstanceProvider.class)
public class NettyClientInstanceProvider implements InstanceProvider<Client> {
    
    @Inject
    private SessionHandler sessionHandler;

    @Inject
    private Codec codec;
    
    @Override
    public Client getInstance() {
        return new NettyClient(codec, sessionHandler);
    }

    @Override
    public Class<Client> getType() {
        return Client.class;
    }
    
    
}
