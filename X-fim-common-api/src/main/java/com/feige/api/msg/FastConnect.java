package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;

@MsgComp(classKey = 2)
public interface FastConnect extends Msg {
    String getClientId();
    
    String getSessionId();
}
