package com.feige.api.config;

import com.feige.api.spi.Spi;

public interface ConfigFactory extends Spi {

    /**
     *
     * @return
     * @throws Exception
     */
    Config create() throws Exception;
}
