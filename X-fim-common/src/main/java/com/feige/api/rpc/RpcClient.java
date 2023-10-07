package com.feige.api.rpc;

import com.feige.api.msg.ChatMsgResp;
import com.feige.fim.protocol.Packet;

public interface RpcClient {
    
    
    ChatMsgResp sendMsg(Packet packet);
    
    
}
