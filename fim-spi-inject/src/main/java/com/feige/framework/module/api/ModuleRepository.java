package com.feige.framework.module.api;

import com.feige.framework.context.api.Lifecycle;

import java.util.List;

public interface ModuleRepository extends Lifecycle {
    
    ModuleContext findModule(String moduleName);

    List<ModuleContext> getModules();

    void addModule(ModuleContext module);

    ModuleContext removeModule(String moduleName);
}
