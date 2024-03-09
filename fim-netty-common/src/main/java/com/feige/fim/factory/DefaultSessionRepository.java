package com.feige.fim.factory;

import com.feige.api.session.AbstractSessionRepository;
import com.feige.api.session.SessionRepository;
import com.feige.utils.spi.annotation.SPI;


@SPI(value="defaultSessionRepository", interfaces = SessionRepository.class)
public class DefaultSessionRepository extends AbstractSessionRepository {


}
