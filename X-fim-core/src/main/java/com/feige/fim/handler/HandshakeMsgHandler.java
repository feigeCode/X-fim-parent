package com.feige.fim.handler;

import com.feige.api.bind.ClientBindManager;
import com.feige.api.cache.Bucket;
import com.feige.api.cache.CacheManager;
import com.feige.api.crypto.Cipher;
import com.feige.api.crypto.CipherFactory;
import com.feige.fim.config.ServerConfigKey;
import com.feige.fim.utils.StringUtils;
import com.feige.fim.utils.crypto.CryptoUtils;
import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.Handshake;
import com.feige.api.constant.Command;
import com.feige.api.handler.AbstractMsgHandler;
import com.feige.api.session.Session;
import com.feige.fim.msg.proto.HandshakeMsgProto;
import com.feige.fim.protocol.Packet;
import com.feige.framework.annotation.Value;
import com.google.auto.service.AutoService;
import org.bouncycastle.util.encoders.Base64;

import java.util.concurrent.TimeUnit;

/**
 * @author feige<br />
 * @ClassName: HandshakeMsgHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/25 21:52<br/>
 */
@SpiComp("handshake")
@AutoService(MsgHandler.class)
public class HandshakeMsgHandler extends AbstractMsgHandler<Packet> {
    public static final String CACHE_NAME = "SESSION_CONTEXT";
    
    @Inject
    private ClientBindManager clientBindManager;
    
    @Inject
    private CacheManager cacheManager;
    
    @Inject
    private CipherFactory cipherFactory;
    
    @Value(ServerConfigKey.SERVER_CRYPTO_AES_KEY_LENGTH)
    private int keyLength;
    
    @Value(ServerConfigKey.SERVER_CRYPTO_ENABLE)
    private boolean enableCrypto;
    
    @Value(ServerConfigKey.SERVER_SESSION_EXPIRE_TIME)
    private long sessionExpireTime;
    
    @Override
    public byte getCmd() {
        return Command.HANDSHAKE.getCmd();
    }

    @Override
    public void handle(Session session, Packet packet) throws RemotingException {
        if (session.isHandshake()) {
            // TODO repeat handshake
            return;
        }
        if (enableCrypto){
            doSecurityHandshake(session, packet);
        }else {
            doHandshake(session, packet);
        }
        
    }
    
    @Override
    public  Class<Handshake> getMsgInterface() {
        return Handshake.class;
    }

    @Override
    public Class<?> getProtoClass() {
        return HandshakeMsgProto.class;
    }

    private void doSecurityHandshake(Session session, Packet packet){
        Handshake handshake = serializedClassManager.getDeserializedObject(packet.getSerializerType(), packet.getClassKey(), packet.getData(), getMsgInterface());
        byte[] clientKey = Base64.decode(handshake.getClientKey());
        byte[] iv = Base64.decode(handshake.getIv());
        byte[] sessionKey = CryptoUtils.mixKey(clientKey, CryptoUtils.randomAesKey(keyLength), keyLength);

        String clientId = handshake.getClientId();
        if (StringUtils.isBlank(clientId) || iv.length != keyLength || clientKey.length != keyLength){
            // TODO error msg and log
            return;
        }
        
        session.setCipher(cipherFactory.create(clientKey, iv));
        
        
        
        
    }


    private void doHandshake(Session session, Packet packet){

    }
    
    
    
    private void setCache(Session session, Handshake handshake){
        Bucket<String> bucket = cacheManager.createBucket(handshake.getClientId(), String.class);
        Cipher cipher = session.getCipher();
        String[] args = cipher.getArgs();
        String[] sessionContext = new String[4 + args.length];
        sessionContext[0] = handshake.getOsName();
        sessionContext[1] = handshake.getOsVersion();
        sessionContext[2] = handshake.getClientVersion();
        sessionContext[3] = handshake.getClientId();
        System.arraycopy(args, 0, sessionContext, 4, args.length);
        bucket.set(StringUtils.commaJoiner.join(sessionContext), sessionExpireTime, TimeUnit.SECONDS);
    }
    
}
