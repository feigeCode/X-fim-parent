package com.feige.fim.session;

import com.feige.api.session.ClientType;

public class SessionInfo {
    private String sid;
    private ClientType clientType;
    
    public SessionInfo(String sid){
        this.sid = sid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }
}
