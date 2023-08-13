package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;

@MsgComp(classKey = 5)
public interface BindClientReq extends Msg{
    
    Class<BindClientReq> TYPE = BindClientReq.class;
    
    String getSessionId();
    
    BindClientReq setSessionId(String sessionId);
    
    String getTags();
    
    BindClientReq setTags(String tags);
    
}
