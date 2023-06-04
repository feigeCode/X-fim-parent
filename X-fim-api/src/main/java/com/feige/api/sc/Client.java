package com.feige.api.sc;

import com.feige.api.handler.RemotingException;

import java.net.InetSocketAddress;

public interface Client extends Service {
    
    

    InetSocketAddress getRemoteAddress();
    
    void reconnect() throws RemotingException;
}
