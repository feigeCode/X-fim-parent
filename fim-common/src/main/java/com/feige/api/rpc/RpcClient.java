package com.feige.api.rpc;

import com.feige.api.sc.Service;
import com.feige.fim.protocol.Packet;

public interface RpcClient<T> extends Service {
    
    Packet exchange(T packet);
    
}
