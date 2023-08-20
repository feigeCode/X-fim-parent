package com.feige.api.sc;

import com.feige.framework.annotation.Inject;
import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.SessionRepository;
import com.feige.fim.utils.StringUtils;

import java.net.InetSocketAddress;

public abstract class AbstractServerProvider implements ServerProvider {


    @Inject
    private SessionHandler sessionHandler;
    
    @Inject
    private Codec codec;
    
    @Inject
    private SessionRepository sessionRepository;
    
    

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
