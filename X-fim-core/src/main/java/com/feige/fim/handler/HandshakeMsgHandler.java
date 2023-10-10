package com.feige.fim.handler;

import com.feige.api.cache.Bucket;
import com.feige.api.cache.CacheManager;
import com.feige.api.constant.Const;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.constant.ProtocolConst.SerializedClass;
import com.feige.api.crypto.Cipher;
import com.feige.api.crypto.CipherFactory;
import com.feige.api.msg.HandshakeResp;
import com.feige.api.sc.Listener;
import com.feige.api.session.SessionContext;
import com.feige.api.constant.ServerConfigKey;
import com.feige.utils.common.StringUtils;
import com.feige.utils.crypto.CryptoUtils;
import com.feige.utils.crypto.Md5Utils;
import com.feige.framework.annotation.Inject;
import com.feige.utils.spi.annotation.SpiComp;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.HandshakeReq;
import com.feige.api.constant.Command;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.framework.utils.Configs;

import org.bouncycastle.util.encoders.Base64;

import java.util.concurrent.TimeUnit;

/**
 * @author feige<br />
 * @ClassName: HandshakeMsgHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/25 21:52<br/>
 */
@SpiComp(value="handshake", interfaces = MsgHandler.class)
public class HandshakeMsgHandler extends AbstractMsgHandler {
    public static final String CACHE_NAME = "SESSION_CONTEXT";
    
    @Inject
    private CacheManager cacheManager;

    @Inject("symmetricEncryption")
    private CipherFactory symmetricCipherFactory;
    
    @Override
    public byte getCmd() {
        return Command.HANDSHAKE.getCmd();
    }

    @Override
    public void handle(Session session, Packet packet) throws RemotingException {
        if (session.isHandshake()) {
            // duplicate handshake
            sendErrorPacket(session, packet, ProtocolConst.ErrorCode.DUPLICATE_HANDSHAKE, "duplicate handshake");
            return;
        }
        handshake(session, packet);
        
    }
    
   
    
    private void handshake(Session session, Packet packet) throws RemotingException {
        HandshakeReq handshakeReq = this.getMsg(packet, HandshakeReq.TYPE);
        if (!validateToken(handshakeReq)) {
            // validate token fail
            sendErrorPacket(session, packet, ProtocolConst.ErrorCode.ILLEGAL_TOKEN, "validate token fail");
            return;
        }
        Boolean enable = Configs.getBoolean(Configs.ConfigKey.CRYPTO_ENABLE, false);
        if (enable){
            doSecurityHandshake(session, packet, handshakeReq);
        }else {
            doHandshake(session, packet, handshakeReq);
        }
    }


    private void doSecurityHandshake(Session session, Packet packet, HandshakeReq handshakeReq) throws RemotingException {
        int keyLength = Configs.getInt(Configs.ConfigKey.CRYPTO_SYMMETRIC_KEY_LENGTH, 16);
        byte[] clientKey = Base64.decode(handshakeReq.getClientKey());
        byte[] iv = Base64.decode(handshakeReq.getIv());
        byte[] serverKey = CryptoUtils.randomAesKey(keyLength);
        byte[] sessionKey = CryptoUtils.mixKey(clientKey, serverKey, keyLength);
        String clientId = handshakeReq.getClientId();
        if (StringUtils.isBlank(clientId) || iv.length != keyLength || clientKey.length != keyLength){
            // illegal key length
            sendErrorPacket(session, packet, ProtocolConst.ErrorCode.ILLEGAL_KEY_LENGTH, "illegal key length");
            return;
        }
        // 根据秘钥生成cipher
        session.setCipher(symmetricCipherFactory.create(clientKey, iv));

        // 创建握手响应包
        Packet handshakeRespPacket = createHandshakeRespPacket(session, handshakeReq, sessionKey, packet);
        
        // 发送响应包
        session.write(handshakeRespPacket, new Listener.CallbackListener(args -> {
            // 根据秘钥生成cipher
            session.setCipher(symmetricCipherFactory.create(sessionKey, iv));

            // 完成握手
            finishHandshake(session, handshakeReq);

        }, Throwable::printStackTrace));
        
    }


    private void doHandshake(Session session, Packet packet, HandshakeReq handshakeReq) throws RemotingException {
        // 创建握手响应包
        Packet handshakeRespPacket = createHandshakeRespPacket(session, handshakeReq, new byte[0], packet);

        // 发送响应包
        session.write(handshakeRespPacket, new Listener.CallbackListener(args -> {
            // 完成握手
            finishHandshake(session, handshakeReq);

        }, Throwable::printStackTrace));
        
    }
    
    private long getSessionExpireTime(){
        return Configs.getLong(ServerConfigKey.SERVER_SESSION_EXPIRE_TIME, Const.DEFAULT_SESSION_EXPIRE_TIME);
    }
    
    private Packet createHandshakeRespPacket(Session session, HandshakeReq handshakeReq, byte[] serverKey, Packet packet){
        long now = System.currentTimeMillis();
        long expireTime = now + getSessionExpireTime() * 1000;
        String sessionId = Md5Utils.digest(handshakeReq.getClientId() + now);
        session.setAttr("sessionId", sessionId);
        session.setAttr("expireTime", expireTime);
        String serverKeyString = Base64.toBase64String(serverKey);
        return this.buildPacket(Command.HANDSHAKE, SerializedClass.HANDSHAKE_RESP, packet, (HandshakeResp handshakeResp) -> {
            handshakeResp.setExpireTime(expireTime)
                    .setServerKey(serverKeyString)
                    .setSessionId(sessionId);
        });
    }
    
    
    private void setCache(Session session, HandshakeReq handshakeReq){
        String sessionId = (String)session.getAttr("sessionId");
        Bucket<String> bucket = cacheManager.createBucket(sessionId);
       
        SessionContext sessionContext = new SessionContext()
                .setOsName(handshakeReq.getOsName()) 
                .setOsVersion(handshakeReq.getOsVersion()) 
                .setClientVersion(handshakeReq.getClientVersion()) 
                .setClientId(handshakeReq.getClientId())
                .setClientType(handshakeReq.getClientType())
                .setExpireTime((Long) session.getAttr("expireTime"));
        Boolean enable = Configs.getBoolean(Configs.ConfigKey.CRYPTO_ENABLE, false);
        if (enable){
            Cipher cipher = session.getCipher();
            String[] args = cipher.getArgs();
            sessionContext.setCipherArgs(args);
        }
        bucket.set(sessionContext.serializeString(), getSessionExpireTime(), TimeUnit.SECONDS);
        session.setAttr("sessionContext", sessionContext);
    }

    private boolean validateToken(HandshakeReq handshakeReq){
        return handshakeReq.getToken() != null;
    }
    
    
    private void finishHandshake(Session session, HandshakeReq handshakeReq){
        // 保存会话信息
        setCache(session, handshakeReq);

        // 标记握手
        session.markHandshake();
        
    }
   
}
