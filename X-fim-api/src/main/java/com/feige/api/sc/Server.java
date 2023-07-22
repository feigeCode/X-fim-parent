package com.feige.api.sc;

import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.SessionRepository;

import java.net.InetSocketAddress;


public interface Server extends Service {
    
    /**
     * get session handler.
     *
     * @return session handler
     */
    SessionHandler getSessionHandler();

    /**
     * get address.
     *
     * @return address.
     */
    InetSocketAddress getAddress();


    /**
     * get codec
     * @return codec
     */
    Codec<?> getCodec();

    /**
     * get session repository
     * @return session repository
     */
    SessionRepository getSessionRepository();
    

}
