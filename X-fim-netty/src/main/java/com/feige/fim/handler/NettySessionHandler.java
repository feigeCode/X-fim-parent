package com.feige.fim.handler;

import com.feige.fim.session.AbstractSessionHandler;

public class NettySessionHandler extends AbstractSessionHandler {
    @Override
    public String getKey() {
        return "nettySessionHandler";
    }
}
