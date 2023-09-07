package com.feige.framework.module;

import com.feige.framework.aware.ApplicationContextAware;
import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.env.api.Environment;
import com.feige.framework.module.api.ModuleContext;
import com.feige.framework.module.api.ModuleRepository;
import com.feige.framework.utils.Configs;
import com.feige.utils.spi.annotation.SpiComp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpiComp(interfaces = ModuleRepository.class)
public class SimpleModuleRepository implements ModuleRepository , ApplicationContextAware {

    
    private final Map<String, ModuleContext> modelContextCache = new ConcurrentHashMap<>(16);
    protected ApplicationContext applicationContext;

    
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
        Environment environment = applicationContext.getEnvironment();
        Collection<String> enabledModules = environment.getCollection(Configs.ConfigKey.ENABLED_MODULE_NAMES);
        for (String enabledModule : enabledModules) {
            addModule(new StandardModuleContext(applicationContext, enabledModule));
        }
    }

    @Override
    public void start(String... args) throws IllegalStateException {
        Collection<ModuleContext> moduleContexts = modelContextCache.values();
        for (ModuleContext moduleContext : moduleContexts) {
            moduleContext.start(args);
        }
    }

    @Override
    public void destroy() throws IllegalStateException {
        Collection<ModuleContext> moduleContexts = modelContextCache.values();
        for (ModuleContext moduleContext : moduleContexts) {
            moduleContext.destroy();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
