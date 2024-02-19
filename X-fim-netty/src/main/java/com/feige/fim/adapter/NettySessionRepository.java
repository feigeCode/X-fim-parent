package com.feige.fim.adapter;

import com.feige.utils.spi.annotation.SPI;
import com.feige.api.session.SessionRepository;
import com.feige.api.session.AbstractSessionRepository;


@SPI(value="nettySessionRepository", interfaces = SessionRepository.class)
public class NettySessionRepository extends AbstractSessionRepository {
    
    
}
