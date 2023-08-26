package com.feige.fim.netty;

import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Client;
import com.feige.framework.api.spi.SpiCompProvider;


@SpiComp(value="nettyClient", interfaces = SpiCompProvider.class, provideTypes = Client.class)
public class NettyClientSpiCompProvider implements SpiCompProvider<Client> {
    
    @Inject
    private SessionHandler sessionHandler;

    @Inject
    private Codec codec;
    
    @Override
    public Client getInstance() {
        return new NettyClient(codec, sessionHandler);
    }
    
    
    
}
