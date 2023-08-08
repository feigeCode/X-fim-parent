package com.feige.api.msg;

public interface HandshakeResp {
    
    String getServerKey();

    HandshakeResp setServerKey(String serverKey);
    
    String getSessionId();

    HandshakeResp setSessionId(String sessionId);
    
    long getExpireTime();

    HandshakeResp setExpireTime(long expireTime);
    
    
}
