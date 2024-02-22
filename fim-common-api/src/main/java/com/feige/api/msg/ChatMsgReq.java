package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;

@MsgComp(classKey = 8)
public interface ChatMsgReq extends Ack {
    Class<ChatMsgReq> TYPE = ChatMsgReq.class;
    
    String getSenderId();
    
    ChatMsgReq setSenderId(String senderId);
    
    String getReceiverId();
    
    ChatMsgReq setReceiverId(String receiverId);
    
    String getContent();
    
    ChatMsgReq setContent(String content);
    
    int getFormat();
    
    ChatMsgReq setFormat(int format);
    
    int getMsgType();
    
    ChatMsgReq setMsgType(int msgType);
    
    int getStatus();
    
    ChatMsgReq setStatus(int status);
    
}
