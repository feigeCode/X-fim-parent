package com.feige.api.msg;

import com.feige.api.annotation.MsgComp;


@MsgComp(classKey = 6)
public interface Ack extends Msg {
    
    Class<Ack> TYPE = Ack.class;
    
    String getServerMsgId();
    
    Ack setServerMsgId(String serverMsgId);

    String getClientMsgId();

    Ack setClientMsgId(String clientMsgIds);
    
    long getSendTime();
    
    Ack setSendTime(long sendTime);
    
    String getExtra();
    
    Ack setExtra(String extra);
    
}
