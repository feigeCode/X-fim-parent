package com.feige.api.msg;

import com.feige.framework.annotation.MsgComp;

@MsgComp(classKey = 1)
public interface Handshake extends Msg {
    
    String getClientKey();
    String getIv();
    String getClientVersion();
    String getClientName();
    int getOsCode();
    String getClientId();
    String getToken();
}
