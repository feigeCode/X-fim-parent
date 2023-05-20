package com.feige.api.config;

import com.feige.api.annotation.LoadOnlyOne;
import com.feige.api.spi.Spi;

@LoadOnlyOne
public interface ConfigFactory extends Spi {

    /**
     *
     * @return
     * @throws Exception
     */
    Config create() throws Exception;
}
