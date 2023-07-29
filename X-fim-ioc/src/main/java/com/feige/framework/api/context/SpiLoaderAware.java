package com.feige.framework.api.context;

import com.feige.framework.api.spi.SpiLoader;

public interface SpiLoaderAware extends Aware{
    
    void setSpiLoader(SpiLoader spiLoader);
}
