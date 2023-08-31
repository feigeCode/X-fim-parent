package com.feige.framework.env;

import com.feige.framework.env.api.Config;
import com.feige.framework.env.api.ConfigFactory;
import com.feige.framework.env.api.Environment;
import com.feige.framework.context.api.LifecycleAdapter;
import com.feige.framework.env.CompositeConfig;
import com.feige.framework.env.EnvConfig;
import com.feige.framework.env.SystemConfig;
import com.feige.framework.utils.Configs;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractEnvironment extends LifecycleAdapter implements Environment {

    private CompositeConfig compositeConfig = new CompositeConfig();
    private Config systemConfig = new SystemConfig();
    private Config envConfig = new EnvConfig();
    private Config appConfig = null;
    private ConfigFactory configFactory;

    @Override
    public void initialize() throws IllegalStateException {
        this.compositeConfig = new CompositeConfig();
        this.systemConfig = new SystemConfig();
        this.envConfig = new EnvConfig();
        String path = this.systemConfig.getString(Configs.CONFIG_FILE_KEY, Configs.DEFAULT_CONFIG_PATH);
        this.appConfig = getConfigFactory().create(Configs.getFile(path));
        this.appConfig.setOrder(2);
        this.compositeConfig.addConfig(this.systemConfig);
        this.compositeConfig.addConfig(this.envConfig);
        this.compositeConfig.addConfig(appConfig);
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
    public Object getObject(String key) {
        return compositeConfig.getObject(key);
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public Config getSystemConfig() {
        return this.systemConfig;
    }

    @Override
    public Config getAppConfig() {
        return this.appConfig;
    }

    @Override
    public Config getEnvConfig() {
        return this.envConfig;
    }
    

    @Override
    public Config getCompositeConfig() {
        return this.compositeConfig;
    }

    @Override
    public void setConfig(String key, Object value) {
        this.appConfig.setConfig(key, value);
    }

    @Override
    public Integer getInt(String key, Integer defaultValue) {
        return compositeConfig.getInt(key, defaultValue);
    }

    @Override
    public Integer getInt(String key) {
        return compositeConfig.getInt(key);
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return compositeConfig.getLong(key, defaultValue);
    }

    @Override
    public Long getLong(String key) {
        return compositeConfig.getLong(key);
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        return compositeConfig.getDouble(key, defaultValue);
    }

    @Override
    public Double getDouble(String key) {
        return compositeConfig.getDouble(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return compositeConfig.getString(key, defaultValue);
    }

    @Override
    public String getString(String key) {
        return compositeConfig.getString(key);
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return compositeConfig.getBoolean(key, defaultValue);
    }

    @Override
    public Boolean getBoolean(String key) {
        return compositeConfig.getBoolean(key);
    }


    @Override
    public Map<String, Object> getMapByKeyPrefix(String key) {
        return compositeConfig.getMapByKeyPrefix(key);
    }

    @Override
    public Collection<String> getCollection(String key) {
        return compositeConfig.getCollection(key);
    }

    @Override
    public <T> T getObject(String key, Class<T> type) {
        return compositeConfig.getObject(key, type);
    }
}
