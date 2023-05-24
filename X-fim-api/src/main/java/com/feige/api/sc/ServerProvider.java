package com.feige.api.sc;

import com.feige.api.spi.Spi;

public interface ServerProvider extends Spi {
    
    Server get();
    
}
