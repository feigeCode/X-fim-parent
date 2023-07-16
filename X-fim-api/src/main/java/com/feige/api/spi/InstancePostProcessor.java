package com.feige.api.spi;

public interface InstancePostProcessor {
   
    default Object postProcessBeforeInitialization(Object instance, String instanceName) {
        return instance;
    }

    
    default Object postProcessAfterInitialization(Object instance, String instanceName) {
        return instance;
    }
}
