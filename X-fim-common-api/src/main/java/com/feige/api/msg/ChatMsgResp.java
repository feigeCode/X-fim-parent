package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;

@MsgComp(classKey = 9)
public interface ChatMsgResp extends Ack {

    Class<ChatMsgResp> TYPE = ChatMsgResp.class;
    
    
    
}
