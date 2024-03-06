package com.feige.api.session;

import com.feige.utils.spi.annotation.SPI;


@SPI(value="nettySessionRepository", interfaces = SessionRepository.class)
public class NettySessionRepository extends AbstractSessionRepository {
    
    
}
