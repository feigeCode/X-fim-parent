package com.feige.fim.handler;

import com.feige.fim.cache.Bucket;
import com.feige.fim.cache.CacheManager;
import com.feige.api.constant.Command;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.crypto.CipherFactory;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.FastConnectReq;
import com.feige.api.msg.SuccessResp;
import com.feige.api.sc.Listener;
import com.feige.api.session.Session;
import com.feige.fim.session.SessionContext;
import com.feige.fim.protocol.Packet;
import com.feige.utils.common.StringUtils;
import com.feige.framework.annotation.Inject;
import com.feige.utils.spi.annotation.SPI;
import com.feige.framework.utils.Configs;
import org.bouncycastle.util.encoders.Base64;

import java.util.Objects;


@SPI(value="fastConnect", interfaces = MsgHandler.class)
public class FastConnectMsgHandler extends AbstractMsgHandler {
    @Inject
    private CacheManager cacheManager;
    
    @Inject("symmetricEncryption")
    private CipherFactory symmetricCipherFactory;
    
    @Override
    public byte getCmd() {
        return Command.FAST_CONNECT.getCmd();
    }

    @Override
    public void handle(Session session, Packet packet) throws RemotingException {
        FastConnectReq fastConnectReq = this.getMsg(packet, FastConnectReq.TYPE);
        String clientId = fastConnectReq.getClientId();
        String sessionId = fastConnectReq.getSessionId();
        SessionContext sessionContext = getCache(sessionId);
        // sessionContext 为空 或者 clientId不相等时快速连接失败
        long expireTime;
        if (sessionContext == null || System.currentTimeMillis() > (expireTime = sessionContext.getExpireTime()) || !Objects.equals(clientId, sessionContext.getClientId())){
            this.sendErrorPacket(session, packet, ProtocolConst.ErrorCode.ILLEGAL_SESSION, "sessionContext is null or expire");
            return;
        }
        setSessionContext(session, sessionContext);
        session.setAttr("sessionId", sessionId);
        session.setAttr("expireTime", expireTime);
        Packet respPacket = createRespPacket(packet);
        session.write(respPacket, new Listener() {
            @Override
            public void onSuccess(Object... args) {
                session.markHandshake();
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
    }
    
    private Packet createRespPacket(Packet packet){
        return this.buildPacket(Command.FAST_CONNECT, ProtocolConst.SerializedClass.SUCCESS_RESP, packet, (SuccessResp successResp) -> {
            successResp.setStatusCode(ProtocolConst.SuccessCode.FAST_CONNECT_SUCCESS.getStatusCode());
        });
    }

    private SessionContext getCache(String sessionId ){
        Bucket<String> bucket = cacheManager.get(sessionId, Bucket.class);
        if (bucket == null){
            return null;
        }
        String sessionContextString = bucket.get();
        if (StringUtils.isBlank(sessionContextString)) {
            return null;
        }
        return new SessionContext().deserialize(sessionContextString);
    }
    
    private void setSessionContext(Session session, SessionContext sessionContext){
        session.setAttr("sessionContext", sessionContext);
        Boolean enable = Configs.getBoolean(Configs.ConfigKey.CRYPTO_ENABLE, false);
        if (enable){
            String[] cipherArgs = sessionContext.getCipherArgs();
            byte[] key1 = new byte[0];
            byte[] key2 = new byte[0];
            Object[] args = new Object[0];
            if (cipherArgs.length >= 1){
               key1 = Base64.decode(cipherArgs[0]);
            }
            if (cipherArgs.length >= 2){
                key2 = Base64.decode(cipherArgs[1]);
            }
            if (cipherArgs.length > 2){
                args = new Object[cipherArgs.length - 2];
                System.arraycopy(cipherArgs, 2, args, 0, cipherArgs.length - 2);
            }
            if (key1.length != 0 || key2.length != 0){
                session.setCipher(symmetricCipherFactory.create(key1, key2, args));
            }
            
        }
    }

    

}
