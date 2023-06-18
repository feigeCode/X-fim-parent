package com.feige.fim.session;

public class SingleSessionHandler extends AbstractSessionHandler {
    
    @Override
    public String getKey() {
        return "nettySessionHandler";
    }
}
