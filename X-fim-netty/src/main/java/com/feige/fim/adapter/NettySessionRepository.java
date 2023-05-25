package com.feige.fim.adapter;

import com.feige.fim.session.AbstractSessionRepository;

public class NettySessionRepository extends AbstractSessionRepository {

    @Override
    public String getKey() {
        return "nettySessionRepository";
    }
    
}
