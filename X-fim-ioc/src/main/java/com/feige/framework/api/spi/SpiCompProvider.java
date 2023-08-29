package com.feige.framework.api.spi;


import com.feige.utils.spi.SpiScope;


public interface SpiCompProvider<T> {
    
    T getInstance();
    
    default SpiScope getScope(){
        return SpiScope.GLOBAL;
    }
}
