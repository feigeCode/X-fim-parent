package com.feige.framework.context;

import com.feige.framework.api.config.Config;
import com.feige.framework.api.config.ConfigFactory;
import com.feige.framework.api.context.Environment;
import com.feige.framework.api.context.LifecycleAdapter;
import com.feige.framework.config.CompositeConfig;
import com.feige.framework.config.EnvConfig;
import com.feige.framework.config.MemoryMapConfig;
import com.feige.framework.config.SystemConfig;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractEnvironment extends LifecycleAdapter implements Environment {

    private CompositeConfig compositeConfig = new CompositeConfig();
    private Config systemConfig = new SystemConfig();
    private Config envConfig = new EnvConfig();
    private Config appConfig = null;
    private Config memoryConfig = null;

    @Override
    public void initialize() throws IllegalStateException {
        this.systemConfig = new SystemConfig();
        this.envConfig = new EnvConfig();
        this.compositeConfig = new CompositeConfig();
        this.memoryConfig = new MemoryMapConfig(new ConcurrentHashMap<>());
        this.compositeConfig.addConfig(this.systemConfig);
        this.compositeConfig.addConfig(this.systemConfig);
        this.compositeConfig.addConfig(this.memoryConfig);
        ServiceLoader<ConfigFactory> loader = ServiceLoader.load(ConfigFactory.class);
        Iterator<ConfigFactory> iterator = loader.iterator();
        if (iterator.hasNext()){
            appConfig = iterator.next().create();
        }else {
            throw new RuntimeException(ConfigFactory.class.getName() + "未发现任何实现，请检查META-INF/services目录下的配置是否正常!");
        }
        this.compositeConfig.addConfig(appConfig);
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
    public Config getMemoryConfig() {
        return this.memoryConfig;
    }

    @Override
    public Config getCompositeConfig() {
        return this.compositeConfig;
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
    public Map<String, Object> getMap(String key) {
        return compositeConfig.getMap(key);
    }

    @Override
    public List<String> getList(String key) {
        return compositeConfig.getList(key);
    }

    @Override
    public String[] getArr(String key) {
        return compositeConfig.getArr(key);
    }

    @Override
    public <T> T getObject(String key, Class<T> type) {
        return compositeConfig.getObject(key, type);
    }
}
