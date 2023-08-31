package com.feige.framework.module.api;

import com.feige.framework.context.api.Lifecycle;

public interface ModuleBootstrap extends Lifecycle {
    
    void run(ModuleContext moduleContext);
    
}
