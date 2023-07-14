package com.feige.fim.server;

import com.feige.api.annotation.Inject;
import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.ServerProvider;
import com.feige.api.session.SessionRepository;
import com.feige.fim.utils.StringUtil;

import java.net.InetSocketAddress;

public abstract class AbstractServerProvider implements ServerProvider {


    @Inject
    protected SessionHandler sessionHandler;
    
    @Inject
    protected Codec codec;
    
    @Inject
    protected SessionRepository sessionRepository;
    
    

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
    protected  Codec getCodec() {
        return this.codec;
    }

    public void setCodec(Codec codec) {
        this.codec = codec;
    }

    /**
     * get session repository
     * @return session repository
     */
    protected SessionRepository getSessionRepository() {
        return this.sessionRepository;
    }

    public void setSessionRepository(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
}
