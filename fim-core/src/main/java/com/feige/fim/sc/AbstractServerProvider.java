package com.feige.fim.sc;

import lombok.Setter;
import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.SessionRepository;
import com.feige.utils.common.StringUtils;

import java.net.InetSocketAddress;

public abstract class AbstractServerProvider implements ServerProvider {


    @Setter
    private SessionHandler sessionHandler;
    
    @Setter
    private Codec codec;
    
    @Setter
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
