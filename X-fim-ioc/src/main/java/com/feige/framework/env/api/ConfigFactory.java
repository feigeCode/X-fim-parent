package com.feige.framework.env.api;


import java.io.File;

public interface ConfigFactory {

    /**
     *
     * @return
     * @throws Exception
     */
    Config create(File file) throws IllegalStateException;
}
