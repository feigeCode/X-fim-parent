package com.feige.fim.adapter;

import com.feige.utils.spi.annotation.SpiComp;
import com.feige.api.session.SessionRepository;
import com.feige.api.session.AbstractSessionRepository;


@SpiComp(value="nettySessionRepository", interfaces = SessionRepository.class)
public class NettySessionRepository extends AbstractSessionRepository {
    
    
}
