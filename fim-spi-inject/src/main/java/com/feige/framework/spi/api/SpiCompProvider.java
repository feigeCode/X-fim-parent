package com.feige.framework.spi.api;


import com.feige.utils.spi.SpiScope;


public interface SpiCompProvider<T> {
    
    T getInstance();
    
    default SpiScope getScope(){
        return SpiScope.GLOBAL;
    }
}
