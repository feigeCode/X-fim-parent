package com.feige.framework.api.config;


import java.io.File;

public interface ConfigFactory {

    /**
     *
     * @return
     * @throws Exception
     */
    Config create(File file) throws IllegalStateException;
}
