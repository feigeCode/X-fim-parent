package com.feige.api.msg;

public interface Handshake extends Msg {
    
    String getClientKey();
    String getIv();
    String geClientVersion();
    String getClientName();
    int getOsCode();
    String getClientId();
    String getToken();
}
