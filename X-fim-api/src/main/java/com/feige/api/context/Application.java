package com.feige.api.context;


public interface Application extends Lifecycle {
    

    /**
     * Running state
     * @return Whether to run
     */
    boolean isRunning();

}
