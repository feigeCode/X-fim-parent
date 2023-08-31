package com.feige.framework.aware;

import com.feige.framework.spi.api.SpiCompLoader;

public interface SpiCompLoaderAware extends Aware {
    
    void setSpiCompLoader(SpiCompLoader spiCompLoader);
}
