package com.feige.fim.rpc;

import com.feige.api.sc.Service;
import com.feige.fim.protocol.Packet;

public interface RpcClient<T> extends Service {
    
    Packet exchange(T packet);
    
}
