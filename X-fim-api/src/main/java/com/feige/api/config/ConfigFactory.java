package com.feige.api.config;

import com.feige.api.annotation.CacheOne;
import com.feige.api.spi.Spi;

@CacheOne
public interface ConfigFactory extends Spi {

    /**
     *
     * @return
     * @throws Exception
     */
    Config create() throws Exception;
}
