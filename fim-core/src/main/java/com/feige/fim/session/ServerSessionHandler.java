package com.feige.fim.session;


import com.feige.api.bind.ClientBindManager;
import com.feige.api.constant.ClientType;
import com.feige.api.crypto.CipherFactory;
import com.feige.api.session.SessionContext;
import com.feige.api.constant.ServerConfigKey;
import com.feige.framework.annotation.Inject;
import com.feige.utils.spi.annotation.SPI;
import com.feige.api.handler.SessionHandler;
import com.feige.fim.handler.AbstractSessionHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.framework.env.api.Environment;
import com.feige.framework.utils.Configs;

import org.bouncycastle.util.encoders.Base64;


@SPI(value="single", interfaces = SessionHandler.class)
public class ServerSessionHandler extends AbstractSessionHandler {

   

    @Inject("asymmetricEncryption")
    private CipherFactory asymmetricCipherFactory;
    
    @Inject
    private ClientBindManager clientBindManager;

    @Override
    public void connected(Session session) throws RemotingException {
        Boolean enable = Configs.getBoolean(Configs.ConfigKey.CRYPTO_ENABLE, false);
        if (enable){
            Environment environment = applicationContext.getEnvironment();
            String priKey = environment.getString(ServerConfigKey.SERVER_CRYPTO_ASYMMETRIC_PRI_K);
            //String pubKey = environment.getString(Configs.ConfigKey.CRYPTO_ASYMMETRIC_PUBLIC_KEY);
            session.setCipher(asymmetricCipherFactory.create(Base64.decode(priKey), new byte[0]));
        }
        super.connected(session);
    }

    @Override
    public void received(Session session, Object message) throws RemotingException {
        super.received(session, message);
    }

    @Override
    public void disconnected(Session session) throws RemotingException {
        SessionContext sessionContext = (SessionContext)session.getAttr("sessionContext");
        if (sessionContext != null){
            clientBindManager.unregister(sessionContext.getClientId(), ClientType.valueOf(sessionContext.getClientType()));
        }
    }
}
