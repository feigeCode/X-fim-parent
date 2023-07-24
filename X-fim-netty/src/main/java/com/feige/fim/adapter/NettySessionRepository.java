package com.feige.fim.adapter;

import com.feige.annotation.SpiComp;
import com.feige.api.session.SessionRepository;
import com.feige.fim.session.AbstractSessionRepository;
import com.google.auto.service.AutoService;

@SpiComp("nettySessionRepository")
@AutoService(SessionRepository.class)
public class NettySessionRepository extends AbstractSessionRepository {
    
    
}
