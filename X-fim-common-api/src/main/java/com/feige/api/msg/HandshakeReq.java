package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;

@MsgComp(classKey = 1)
public interface HandshakeReq extends Msg {
    
    String getClientKey();
    HandshakeReq setClientKey(String clientKey);
    String getIv();
    HandshakeReq setIv(String iv);
    String getClientVersion();
    HandshakeReq setClientVersion(String clientVersion);
    String getOsName();
    HandshakeReq setOsName(String osName);
    String getOsVersion();
    HandshakeReq setOsVersion(String osVersion);
    int getClientType();
    HandshakeReq setClientType(int clientType);
    String getClientId();
    HandshakeReq setClientId(String clientId);
    String getToken();
    HandshakeReq setToken(String token);
}
