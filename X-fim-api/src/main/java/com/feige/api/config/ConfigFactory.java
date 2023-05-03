package com.feige.api.config;

import com.feige.api.annotation.LoadOnlyTheFirstOne;
import com.feige.api.spi.Spi;

@LoadOnlyTheFirstOne
public interface ConfigFactory extends Spi {

    /**
     *
     * @return
     * @throws Exception
     */
    Config create() throws Exception;
}