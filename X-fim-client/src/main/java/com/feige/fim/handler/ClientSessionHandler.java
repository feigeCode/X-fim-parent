package com.feige.fim.handler;

import com.feige.fim.api.SessionStorage;
import com.feige.fim.config.ClientConfig;
import com.feige.fim.config.ClientConfigKey;
import com.feige.fim.utils.StringUtils;
import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.api.handler.RemotingException;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.Session;
import com.feige.api.constant.Command;
import com.feige.fim.protocol.Packet;
import com.feige.framework.context.AppContext;
import com.google.auto.service.AutoService;

import java.util.List;


@AutoService(SessionHandler.class)
@SpiComp
public class ClientSessionHandler extends AbstractSessionHandler {

    @Inject
    private SessionStorage sessionStorage;
    
    
    @Override
    public void connected(Session session) throws RemotingException {
        
    }
    
    private void tryFastConnect(Session session) throws RemotingException {
        String sessionConfig = sessionStorage.getItem(ClientConfigKey.SESSION_PERSISTENT_KEY);
        if (StringUtils.isBlank(sessionConfig)){
            handshake(session);
        }
        
        
    }
    
    private void handshake(Session session) throws RemotingException {
        
        Packet packet = Packet.create(Command.HANDSHAKE);
        session.write(packet);
    }
}
