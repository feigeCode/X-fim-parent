package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;

@MsgComp(classKey = 1)
public interface Handshake extends Msg {
    
    String getClientKey();
    String getIv();
    String getClientVersion();
    String getOsName();
    String getOsVersion();
    int getClientType();
    String getClientId();
    String getToken();
}
