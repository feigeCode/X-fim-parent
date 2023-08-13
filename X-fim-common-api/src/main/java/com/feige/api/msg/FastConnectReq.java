package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;

@MsgComp(classKey = 2)
public interface FastConnectReq extends Msg {
    
    Class<FastConnectReq> TYPE = FastConnectReq.class;
    
    String getClientId();

    FastConnectReq setClientId(String clientId);
    
    String getSessionId();

    FastConnectReq setSessionId(String sessionId);
}
