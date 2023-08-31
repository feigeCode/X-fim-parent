package com.feige.framework.context.api;


import com.feige.framework.env.api.Environment;
import com.feige.framework.inject.CompInjection;
import com.feige.framework.module.api.ModuleContext;
import com.feige.framework.registry.CompRegistry;
import com.feige.framework.spi.api.SpiCompLoader;

import java.util.List;


public interface ApplicationContext extends CompFactory, CompRegistry {

    enum AppState {CREATED, INITIALIZED, DESTROY}

    ModuleContext findModule(String moduleName);

    List<ModuleContext> getModules();

    void addModule(ModuleContext module);

    ModuleContext removeModule(String moduleName);
    
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
