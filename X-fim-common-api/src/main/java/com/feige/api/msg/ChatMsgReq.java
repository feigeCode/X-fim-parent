package com.feige.api.msg;

public interface ChatMsgReq extends Ack {
    String getSenderId();
    
    ChatMsgReq setSenderId(String senderId);
    
    String getReceiverId();
    
    ChatMsgReq setReceiverId(String receiverId);
    
    String getContent();
    
    ChatMsgReq setContent(String content);
    
}
