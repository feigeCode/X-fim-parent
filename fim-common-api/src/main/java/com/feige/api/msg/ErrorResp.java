package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;

@MsgComp(classKey = 7)
public interface ErrorResp extends Msg {
    Class<ErrorResp> TYPE = ErrorResp.class;
    
    int getErrorCode();
    
    ErrorResp setErrorCode(int errorCode);
    
    String getReason();
    
    ErrorResp setReason(String reason);
    
    String getExtra();
    
    ErrorResp setExtra(String extra);
    
}
