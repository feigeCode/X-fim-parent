package com.feige.framework.context;

import com.feige.framework.api.config.Config;
import com.feige.framework.api.config.ConfigFactory;
import com.feige.framework.api.context.Environment;
import com.feige.framework.api.context.LifecycleAdapter;
import com.feige.framework.api.context.ModuleContext;
import com.feige.framework.api.context.ModuleEnvironment;
import com.feige.framework.config.CompositeConfig;


public abstract class AbstractModuleEnvironment extends LifecycleAdapter implements ModuleEnvironment {
    
    private final ModuleContext moduleContext;
    private final Environment parent;
    private CompositeConfig compositeConfig;
    private Config moduleConfig;
    private ConfigFactory configFactory;

    public AbstractModuleEnvironment(Environment parent, ModuleContext moduleContext) {
        this.parent = parent;
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
        return parent.getSystemConfig();
    }

    @Override
    public Config getAppConfig() {
        return parent.getAppConfig();
    }

    @Override
    public Config getEnvConfig() {
        return parent.getEnvConfig();
    }

    @Override
    public Config getCompositeConfig() {
        return parent.getCompositeConfig();
    }

   

    @Override
    public void initialize() throws IllegalStateException {
        this.compositeConfig = new CompositeConfig();
        this.moduleConfig = getConfigFactory().create();
        this.moduleConfig.setOrder(parent.order() - 1);
        this.compositeConfig.addConfig(this.moduleConfig);
        this.compositeConfig.addConfig(parent);
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
        return parent;
    }
}
