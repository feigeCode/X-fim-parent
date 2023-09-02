package com.feige.framework.context.api;


import com.feige.framework.comp.api.CompNameGenerate;
import com.feige.framework.env.api.Environment;
import com.feige.framework.factory.api.CompFactory;
import com.feige.framework.inject.api.CompInjection;
import com.feige.framework.instantiate.api.InstantiationStrategy;
import com.feige.framework.module.api.ModuleRepository;
import com.feige.framework.processor.api.CompPostProcessor;
import com.feige.framework.registry.CompRegistry;
import com.feige.framework.spi.api.SpiCompLoader;

import java.util.List;


public interface ApplicationContext extends CompFactory, CompRegistry, ModuleRepository {

    enum AppState {CREATED, INITIALIZED, DESTROY}

    ModuleRepository getModuleRepository();
    
    ApplicationContext getParent();
    
    CompRegistry getCompRegistry();
    
    CompFactory getCompFactory();

    Environment getEnvironment();
    
    SpiCompLoader getSpiCompLoader();
    
    InstantiationStrategy getInstantiationStrategy();
    
    CompInjection getCompInjection();
    
    
    CompNameGenerate getCompNameGenerate();
    
    
    ClassLoader getClassLoader();
    
    
    List<CompPostProcessor> getPostProcessors();
    
   

}
