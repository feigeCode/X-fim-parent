package com.feige.fim.netty;

import com.feige.api.sc.ClientProvider;
import com.feige.framework.annotation.Inject;
import com.feige.utils.spi.annotation.SPI;
import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Client;


@SPI(value="nettyClient", interfaces = ClientProvider.class)
public class NettyClientProvider implements ClientProvider {
    
    @Inject
    private SessionHandler sessionHandler;

    @Inject
    private Codec codec;
    
    @Override
    public Client get() {
        return new NettyClient(codec, sessionHandler);
    }
    
    
    
}
