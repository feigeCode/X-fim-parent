package com.feige.framework.api.context;


import com.feige.framework.api.spi.SpiLoader;

public interface ApplicationContext extends SpiLoader {
    

    Environment getEnvironment();
    
    SpiLoader getSpiLoader();

}
