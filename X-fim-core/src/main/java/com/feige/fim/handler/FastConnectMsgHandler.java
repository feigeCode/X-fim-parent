package com.feige.fim.handler;

import com.feige.api.cache.Bucket;
import com.feige.api.cache.CacheManager;
import com.feige.api.constant.Command;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.crypto.CipherFactory;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.FastConnectReq;
import com.feige.api.msg.FastConnectResp;
import com.feige.api.sc.Listener;
import com.feige.api.session.Session;
import com.feige.api.session.SessionContext;
import com.feige.fim.protocol.Packet;
import com.feige.fim.utils.StringUtils;
import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.framework.utils.Configs;
import org.bouncycastle.util.encoders.Base64;

import java.util.Objects;


@SpiComp(value="fastConnect", interfaces = MsgHandler.class)
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
            throw new RemotingException(session, "sessionContext is null or expire");
        }
        setSessionContext(session, sessionContext);
        session.setAttr("sessionId", sessionId);
        session.setAttr("expireTime", expireTime);
        Packet respPacket = createRespPacket(packet, 1);
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
    
    private Packet createRespPacket(Packet packet, int statusCode){
        byte serializerType = packet.getSerializerType();
        byte classKey = ProtocolConst.SerializedClass.FAST_CONNECT_RESP.getClassKey();
        Packet respPacket = Packet.create(Command.FAST_CONNECT);
        respPacket.setSerializerType(serializerType);
        respPacket.setClassKey(classKey);
        respPacket.setSequenceNum(packet.getSequenceNum() + 1);
        FastConnectResp fastConnectResp = serializedClassManager.newObject(serializerType, classKey);
        fastConnectResp.setStatusCode(statusCode);
        respPacket.setData(serializedClassManager.getSerializedObject(serializerType, fastConnectResp));
        return respPacket;
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
