package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;

@MsgComp(classKey = 3)
public interface HandshakeResp extends Msg{
    Class<HandshakeResp> HANDSHAKE_RESP_CLASS = HandshakeResp.class;
    String getServerKey();

    HandshakeResp setServerKey(String serverKey);
    
    String getSessionId();

    HandshakeResp setSessionId(String sessionId);
    
    long getExpireTime();

    HandshakeResp setExpireTime(long expireTime);
    
    
}
