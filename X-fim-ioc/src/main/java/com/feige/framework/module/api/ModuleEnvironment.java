package com.feige.framework.module.api;

import com.feige.framework.env.api.Config;
import com.feige.framework.env.api.Environment;

public interface ModuleEnvironment extends Environment {
    
    Environment getParent();
    
    Config getModuleConfig();
}
