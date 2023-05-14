package com.feige.api.sc;

import com.feige.api.handler.RemotingException;

public interface Client extends Endpoint {
    
    

    void reconnect() throws RemotingException;
}
