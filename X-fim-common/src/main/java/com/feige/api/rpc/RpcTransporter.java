package com.feige.api.rpc;

import com.feige.framework.context.api.Lifecycle;

public interface RpcTransporter<T> extends Lifecycle {
    
    RpcServer<T> rpcServer();
    
    
    RpcClient<T> rpcClient();
    
}
