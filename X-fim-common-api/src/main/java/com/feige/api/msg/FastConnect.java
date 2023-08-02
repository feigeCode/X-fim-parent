package com.feige.api.msg;

import com.feige.framework.annotation.MsgComp;

@MsgComp(classKey = 2)
public interface FastConnect extends Msg {
    String getClientId();
    
    String getSessionId();
}
