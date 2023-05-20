package com.feige.fim.config.impl;

import com.feige.api.config.Config;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author feige<br />
 * @ClassName: SystemConfig <br/>
 * @Description: <br/>
 * @date: 2023/5/20 10:08<br/>
 */
public class SystemConfig implements Config {
    private final Properties prop = System.getProperties();
    @Override
    public void parseConfig(Object obj) throws Exception {
        
    }

    @Override
    public Integer getInt(String key, Integer defaultValue) {
        return (Integer) prop.getOrDefault(key, defaultValue);
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return (Long) prop.getOrDefault(key, defaultValue);
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        return (Double) prop.getOrDefault(key, defaultValue);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return (Boolean) prop.getOrDefault(key, defaultValue);
    }

    @Override
    public Map<String, Object> getMap(String key) {
        return null;
    }

    @Override
    public List<String> getList(String key) {
        return null;
    }

    @Override
    public String[] getArr(String key) {
        return null;
    }

    @Override
    public Object getObject(String key) {
        return this.prop.get(key);
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }
}
