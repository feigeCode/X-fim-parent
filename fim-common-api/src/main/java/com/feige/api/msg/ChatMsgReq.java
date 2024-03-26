package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;

@MsgComp(classKey = 8)
public interface ChatMsgReq extends Msg {
    Class<ChatMsgReq> TYPE = ChatMsgReq.class;

    String getId();

    ChatMsgReq setId(String id);
    
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

    long getSendTime();

    ChatMsgReq setSendTime(long sendTime);

    String getExtra();

    ChatMsgReq setExtra(String extra);
}
