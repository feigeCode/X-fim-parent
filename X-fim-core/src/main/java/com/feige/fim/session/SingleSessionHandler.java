package com.feige.fim.session;


import com.feige.api.crypto.CipherFactory;
import com.feige.fim.config.ServerConfigKey;
import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.api.handler.SessionHandler;
import com.feige.fim.handler.AbstractSessionHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.framework.api.context.Environment;
import com.feige.framework.utils.Configs;

import org.bouncycastle.util.encoders.Base64;


@SpiComp(value="single", interfaces = SessionHandler.class)
public class SingleSessionHandler extends AbstractSessionHandler {

   

    @Inject("asymmetricEncryption")
    private CipherFactory asymmetricCipherFactory;

    @Override
    public void connected(Session session) throws RemotingException {
        Boolean enable = Configs.getBoolean(Configs.ConfigKey.CRYPTO_ENABLE, false);
        if (enable){
            Environment environment = applicationContext.getEnvironment();
            String priKey = environment.getString(ServerConfigKey.SERVER_CRYPTO_ASYMMETRIC_PRI_K);
            session.setCipher(asymmetricCipherFactory.create(Base64.decode(priKey), new byte[0]));
        }
        super.connected(session);
    }

    @Override
    public void received(Session session, Object message) throws RemotingException {
        super.received(session, message);
    }
}
