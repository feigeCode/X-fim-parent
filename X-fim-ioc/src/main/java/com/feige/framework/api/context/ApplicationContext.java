package com.feige.framework.api.context;


import com.feige.framework.api.spi.InstantiationStrategy;
import com.feige.framework.api.spi.SpiCompLoader;

import java.util.List;


public interface ApplicationContext extends CompFactory {
    
    CompFactory getCompFactory();

    Environment getEnvironment();
    
    SpiCompLoader getSpiCompLoader();
    
    
    InstantiationStrategy getInstantiationStrategy();
    
    
    CompInjection getCompInjection();
    
    
    CompNameGenerate getCompNameGenerate();
    
    
    ClassLoader getClassLoader();
    
    
    List<CompPostProcessor> getPostProcessors();

}
