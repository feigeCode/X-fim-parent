package com.feige.framework.api.context;

public interface ModuleBootstrap extends Lifecycle{
    
    void run(ModuleContext moduleContext);
    
}
