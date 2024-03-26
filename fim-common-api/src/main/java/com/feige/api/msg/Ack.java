package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;


@MsgComp(classKey = 6)
public interface Ack extends Msg {
    
    Class<Ack> TYPE = Ack.class;

    String getSenderId();

    Ack setSenderId(String senderId);

    String getReceiverId();

    Ack setReceiverId(String receiverId);
    
    String getMsgId();
    
    Ack setMsgId(String msgId);

    int getSeqId();

    Ack setSeqId(int seqId);
    
    long getSendTime();
    
    Ack setSendTime(long sendTime);
    
    String getExtra();
    
    Ack setExtra(String extra);
    
}
