package com.feige.framework.module;

import com.feige.framework.module.api.ModuleContext;
import com.feige.framework.module.api.ModuleRepository;
import com.feige.utils.spi.annotation.SpiComp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpiComp(interfaces = ModuleRepository.class)
public class SimpleModuleRepository implements ModuleRepository {

    protected final Map<String, ModuleContext> modelContextCache = new ConcurrentHashMap<>(16);

    @Override
    public ModuleContext findModule(String moduleName) {
        return modelContextCache.get(moduleName);
    }

    @Override
    public List<ModuleContext> getModules() {
        return new ArrayList<>(modelContextCache.values());
    }

    @Override
    public void addModule(ModuleContext module) {
        module.initialize();
        modelContextCache.put(module.moduleName(), module);
    }

    @Override
    public ModuleContext removeModule(String moduleName) {
        ModuleContext moduleContext = modelContextCache.remove(moduleName);
        moduleContext.destroy();
        return moduleContext;
    }

    @Override
    public void initialize() throws IllegalStateException {
        
    }

    @Override
    public void start() throws IllegalStateException {

    }

    @Override
    public void destroy() throws IllegalStateException {
        Collection<ModuleContext> values = modelContextCache.values();
        for (ModuleContext moduleContext : values) {
            moduleContext.destroy();
        }
    }
}
