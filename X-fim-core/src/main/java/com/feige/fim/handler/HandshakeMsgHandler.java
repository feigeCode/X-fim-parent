package com.feige.fim.handler;

import com.feige.api.bind.ClientBindManager;
import com.feige.api.cache.Bucket;
import com.feige.api.cache.CacheManager;
import com.feige.api.constant.ProtocolConst.SerializedClass;
import com.feige.api.crypto.Cipher;
import com.feige.api.crypto.CipherFactory;
import com.feige.api.msg.HandshakeResp;
import com.feige.api.msg.Msg;
import com.feige.fim.config.ServerConfigKey;
import com.feige.fim.msg.proto.HandshakeReqProto;
import com.feige.fim.msg.proto.HandshakeRespProto;
import com.feige.fim.utils.Pair;
import com.feige.fim.utils.StringUtils;
import com.feige.fim.utils.crypto.CryptoUtils;
import com.feige.fim.utils.crypto.Md5Utils;
import com.feige.framework.annotation.InitMethod;
import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.HandshakeReq;
import com.feige.api.constant.Command;
import com.feige.api.handler.AbstractMsgHandler;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.framework.annotation.Value;
import com.feige.framework.utils.Configs;
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

    @Inject("symmetricEncryption")
    private CipherFactory symmetricCipherFactory;
    
    @Value(ServerConfigKey.SERVER_SESSION_EXPIRE_TIME)
    private long sessionExpireTime;
    
    @InitMethod
    public void initHandshakeResp(){
        genClass(HandshakeResp.class, Pair.of(HandshakeRespProto.class, HandshakeRespProto.Builder.class));
    }
    
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
        Boolean enable = Configs.getBoolean(Configs.ConfigKey.CRYPTO_ENABLE, false);
        if (enable){
            doSecurityHandshake(session, packet);
        }else {
            doHandshake(session, packet);
        }
        
    }
    
    @Override
    public  Class<HandshakeReq> getMsgInterface() {
        return HandshakeReq.class;
    }

    @Override
    public Pair<Class<?>, Class<?>> getProtoClass() {
        return Pair.of(HandshakeReqProto.class, HandshakeReqProto.Builder.class);
    }

    private void doSecurityHandshake(Session session, Packet packet) throws RemotingException {
        int keyLength = Configs.getInt(Configs.ConfigKey.CRYPTO_SYMMETRIC_KEY_LENGTH, 16);
        byte serializerType = packet.getSerializerType();
        HandshakeReq handshakeReq = serializedClassManager.getDeserializedObject(serializerType, packet.getClassKey(), packet.getData(), getMsgInterface());
        byte[] clientKey = Base64.decode(handshakeReq.getClientKey());
        byte[] iv = Base64.decode(handshakeReq.getIv());
        byte[] serverKey = CryptoUtils.randomAesKey(keyLength);
        byte[] sessionKey = CryptoUtils.mixKey(clientKey, serverKey, keyLength);
        String clientId = handshakeReq.getClientId();
        if (StringUtils.isBlank(clientId) || iv.length != keyLength || clientKey.length != keyLength){
            // TODO error msg and log
            return;
        }
        // 根据秘钥生成cipher
        session.setCipher(symmetricCipherFactory.create(clientKey, iv));

        // 创建握手响应包
        Packet handshakeRespPacket = createHandshakeRespPacket(handshakeReq, serverKey, packet);
        
        // 发送响应包
        session.write(handshakeRespPacket);

        // 根据秘钥生成cipher
        session.setCipher(symmetricCipherFactory.create(sessionKey, iv));
        
        // 保存会话信息
        setCache(session, handshakeReq);
    }


    private void doHandshake(Session session, Packet packet){

    }
    
    
    private Packet createHandshakeRespPacket(HandshakeReq handshakeReq, byte[] serverKey, Packet packet){
        byte serializerType = packet.getSerializerType();
        HandshakeResp handshakeResp = serializedClassManager.newObject(serializerType, SerializedClass.HANDSHAKE_RESP.getClassKey());
        long now = System.currentTimeMillis();
        long expireTime = now + sessionExpireTime * 1000;
        String sessionId = Md5Utils.encrypt(handshakeReq.getClientId() + now);
        handshakeResp.setExpireTime(expireTime)
                .setServerKey(Base64.toBase64String(serverKey))
                .setSessionId(sessionId);
        Packet packetResp = Packet.create(Command.HANDSHAKE);
        packetResp.setSequenceNum(packetResp.getSequenceNum() + 1);
        packetResp.setSerializerType(serializerType);
        packetResp.setClassKey(SerializedClass.HANDSHAKE_RESP.getClassKey());
        packetResp.setData(serializedClassManager.getSerializedObject(serializerType, handshakeResp));
        return packetResp;
    }
    
    
    private void setCache(Session session, HandshakeReq handshakeReq){
        Bucket<String> bucket = cacheManager.createBucket(handshakeReq.getClientId(), String.class);
        Cipher cipher = session.getCipher();
        String[] args = cipher.getArgs();
        String[] sessionContext = new String[4 + args.length];
        sessionContext[0] = handshakeReq.getOsName();
        sessionContext[1] = handshakeReq.getOsVersion();
        sessionContext[2] = handshakeReq.getClientVersion();
        sessionContext[3] = handshakeReq.getClientId();
        System.arraycopy(args, 0, sessionContext, 4, args.length);
        bucket.set(StringUtils.commaJoiner.join(sessionContext), sessionExpireTime, TimeUnit.SECONDS);
    }
    
}
