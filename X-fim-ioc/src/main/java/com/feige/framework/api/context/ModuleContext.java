package com.feige.framework.api.context;


public interface ModuleContext extends ApplicationContext {
    
    ApplicationContext getParent();
    
    String moduleName();
    
}
