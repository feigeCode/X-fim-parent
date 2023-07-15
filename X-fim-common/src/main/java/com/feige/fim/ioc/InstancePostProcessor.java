package com.feige.fim.ioc;

public interface InstancePostProcessor {
   
    default Object postProcessBeforeInitialization(Object instance, String instanceName) throws Exception {
        return instance;
    }

    
    default Object postProcessAfterInitialization(Object instance, String instanceName) throws Exception {
        return instance;
    }
}
