package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;


@MsgComp(classKey = 6)
public interface Ack extends Msg {
    
    Class<Ack> TYPE = Ack.class;
    
    String getMsgId();
    
    Ack setMsgId(String msgId);

    int getSequenceNum();

    Ack setSequenceNum(int sequenceNum);
    
    long getSendTime();
    
    Ack setSendTime(long sendTime);
    
    String getExtra();
    
    Ack setExtra(String extra);
    
}
