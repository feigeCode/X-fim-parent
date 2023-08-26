package com.feige.framework.api.spi;

public interface SpiCompProvider<T> {
    
    T getInstance();
    
    default boolean isSingleton() {
        return true;
    }
}
