package com.feige.framework.api.context;

import com.feige.framework.api.spi.SpiCompLoader;

public interface SpiCompLoaderAware extends Aware{
    
    void setSpiCompLoader(SpiCompLoader spiCompLoader);
}
