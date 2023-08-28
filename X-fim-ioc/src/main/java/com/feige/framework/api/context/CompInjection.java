package com.feige.framework.api.context;

import com.feige.framework.annotation.SPI;

@SPI
public interface CompInjection extends ApplicationContextAware{
    
    
    void inject(Object comp);
}
