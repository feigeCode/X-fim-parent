package com.feige.framework.api.spi;

public interface InstanceProvider<T> {
    
    T getInstance();
    
    
    Class<T> getType();
    
    default boolean isSingleton() {
        return true;
    }
}
