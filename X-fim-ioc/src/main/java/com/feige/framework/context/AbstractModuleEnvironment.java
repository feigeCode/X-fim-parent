package com.feige.framework.context;

import com.feige.framework.api.config.Config;
import com.feige.framework.api.config.ConfigFactory;
import com.feige.framework.api.context.Environment;
import com.feige.framework.api.context.LifecycleAdapter;
import com.feige.framework.api.context.ModuleContext;
import com.feige.framework.api.context.ModuleEnvironment;
import com.feige.framework.config.CompositeConfig;
import com.feige.framework.utils.Configs;

import java.io.File;


public abstract class AbstractModuleEnvironment extends LifecycleAdapter implements ModuleEnvironment {
    
    private final ModuleContext moduleContext;
    private CompositeConfig compositeConfig;
    private Config moduleConfig;
    private ConfigFactory configFactory;

    public AbstractModuleEnvironment(ModuleContext moduleContext) {
        this.moduleContext = moduleContext;
    }

    @Override
    public Config getModuleConfig() {
        return moduleConfig;
    }

    @Override
    public Object getObject(String key) {
        return this.compositeConfig.getObject(key);
    }

    @Override
    public int order() {
        return 0;
    }
    
    @Override
    public Config getSystemConfig() {
        return getParent().getSystemConfig();
    }

    @Override
    public Config getAppConfig() {
        return getParent().getAppConfig();
    }

    @Override
    public Config getEnvConfig() {
        return getParent().getEnvConfig();
    }

    @Override
    public Config getCompositeConfig() {
        return getParent().getCompositeConfig();
    }

   

    @Override
    public void initialize() throws IllegalStateException {
        this.compositeConfig = new CompositeConfig();
        String moduleName = moduleContext.moduleName();
        String modulesDir = getParent().getString(Configs.ConfigKey.MODULES_DIR_KEY, Configs.DEFAULT_MODULES_DIR);
        modulesDir = modulesDir + moduleName + File.separator + "fim-module.";
        this.moduleConfig = getConfigFactory().create(Configs.getFile(modulesDir));
        this.moduleConfig.setOrder(getParent().order() - 1);
        this.compositeConfig.addConfig(this.moduleConfig);
        this.compositeConfig.addConfig(getParent());
    }
    
    @Override
    public void setConfigFactory(ConfigFactory configFactory) {
        this.configFactory = configFactory;
    }

    @Override
    public ConfigFactory getConfigFactory() {
        return configFactory;
    }


    @Override
    public Environment getParent() {
        return moduleContext.getParent().getEnvironment();
    }
}
