package com.feige.framework.spi.api;


public interface SpiCompProvider<T> {
    
    T getInstance();
    
    default SpiScope getScope(){
        return SpiScope.GLOBAL;
    }
}
