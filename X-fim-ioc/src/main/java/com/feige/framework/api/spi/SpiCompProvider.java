package com.feige.framework.api.spi;


import com.feige.framework.annotation.SPI;
import com.feige.utils.spi.SpiScope;

@SPI
public interface SpiCompProvider<T> {
    
    T getInstance();
    
    default SpiScope getScope(){
        return SpiScope.GLOBAL;
    }
}
