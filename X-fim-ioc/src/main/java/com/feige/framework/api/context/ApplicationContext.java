package com.feige.framework.api.context;


import com.feige.framework.api.spi.SpiCompLoader;

import java.util.List;


public interface ApplicationContext extends CompFactory {

    enum AppState {CREATED, INITIALIZED, DESTROY}

    ModuleContext findModule(String moduleName);

    List<ModuleContext> getModules();

    void addModule(ModuleContext module);

    ModuleContext removeModule(String moduleName);
    
    List<CompFactory> getCompFactories();

    Environment getEnvironment();
    
    SpiCompLoader getSpiCompLoader();
    
    InstantiationStrategy getInstantiationStrategy();
    
    CompInjection getCompInjection();
    
    
    CompNameGenerate getCompNameGenerate();
    
    
    ClassLoader getClassLoader();
    
    
    List<CompPostProcessor> getPostProcessors();
    
   

}
