package com.feige.framework.api.context;


import com.feige.framework.api.spi.SpiCompLoader;

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
