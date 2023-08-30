package com.feige.framework.api.context;

import com.feige.framework.api.config.Config;

public interface ModuleEnvironment extends Environment {
    
    Environment getParent();
    
    Config getModuleConfig();
}
