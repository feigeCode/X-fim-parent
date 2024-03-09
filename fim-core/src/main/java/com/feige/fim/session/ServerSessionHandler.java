package com.feige.fim.session;


import com.feige.fim.bind.ClientBindManager;
import com.feige.api.constant.ClientType;
import com.feige.fim.constant.ServerConfigKey;
import com.feige.api.crypto.CipherFactory;
import com.feige.api.handler.RemotingException;
import com.feige.api.handler.SessionHandler;
import com.feige.api.session.Session;
import com.feige.api.session.SessionRepository;
import com.feige.handler.AbstractSessionHandler;
import com.feige.framework.annotation.Inject;
import com.feige.framework.env.api.Environment;
import com.feige.framework.utils.Configs;
import com.feige.utils.spi.annotation.SPI;
import org.bouncycastle.util.encoders.Base64;


@SPI(value="single", interfaces = SessionHandler.class)
public class ServerSessionHandler extends AbstractSessionHandler {
    private final static String HEARTBEAT_CNT = "hb_cnt";
    private final static int MAX_TIMEOUT_CNT = 3;
    @Inject("asymmetricEncryption")
    private CipherFactory asymmetricCipherFactory;
    
    @Inject
    private ClientBindManager clientBindManager;

    @Inject
    private SessionRepository sessionRepository;

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
        session.removeAttr(HEARTBEAT_CNT);
    }

    @Override
    public void disconnected(Session session) throws RemotingException {
        sessionRepository.removeAndClose(session);
        SessionContext sessionContext = (SessionContext)session.getAttr("sessionContext");
        if (sessionContext != null){
            clientBindManager.unregister(sessionContext.getClientId(), ClientType.valueOf(sessionContext.getClientType()));
        }
    }
}
