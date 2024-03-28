package com.feige.fim.client;

import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Client;
import com.feige.api.sc.ClientProvider;
import com.feige.api.session.SessionRepository;
import lombok.Setter;
import com.feige.utils.spi.annotation.SPI;


@SPI(value="nettyClient", interfaces = ClientProvider.class)
public class NettyClientProvider implements ClientProvider {
    
    @Setter
    private SessionHandler sessionHandler;

    @Setter
    private SessionRepository sessionRepository;

    @Setter
    private Codec codec;
    
    @Override
    public Client get() {
        return new NettyClient(codec, sessionHandler, sessionRepository);
    }
    
    
    
}
