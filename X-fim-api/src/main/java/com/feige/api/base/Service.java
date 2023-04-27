package com.feige.api.base;


import java.util.Map;

public interface Service {

    /**
     * initialization
     * @param args arguments
     */
    void init(Map<String, Object> args);

    /**
     * start service
     */
    void start() throws Exception;

    /**
     * stop service
     */
    void stop();

    /**
     * Running state
     * @return Whether to run
     */
    boolean isRunning();

}
