package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;

@MsgComp(classKey = 2)
public interface FastConnect extends Msg {
    String getClientId();

    FastConnect setClientId(String clientId);
    
    String getSessionId();

    FastConnect setSessionId(String sessionId);
}
