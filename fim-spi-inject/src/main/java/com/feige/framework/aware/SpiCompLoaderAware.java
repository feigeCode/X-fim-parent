package com.feige.framework.aware;

import com.feige.framework.annotation.DisableInject;
import com.feige.framework.spi.api.SpiCompLoader;

public interface SpiCompLoaderAware extends Aware {

    @DisableInject
    void setSpiCompLoader(SpiCompLoader spiCompLoader);
}
