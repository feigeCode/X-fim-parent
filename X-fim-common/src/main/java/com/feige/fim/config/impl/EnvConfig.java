package com.feige.fim.config.impl;

import com.feige.api.config.Config;

import java.util.List;
import java.util.Map;

/**
 * @author feige<br />
 * @ClassName: EnvConfig <br/>
 * @Description: <br/>
 * @date: 2023/5/20 15:24<br/>
 */
public class EnvConfig implements Config {
    
    private final Map<String, String> envMap = System.getenv();
    @Override
    public void parseConfig(Object config) throws Exception {
        
    }

    @Override
    public Integer getInt(String key, Integer defaultValue) {
        return defaultValue;
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return defaultValue;
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        return defaultValue;
    }

    @Override
    public String getString(String key, String defaultValue) {
        return System.getenv(key);
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return defaultValue;
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
        return new String[0];
    }

    @Override
    public Object getObject(String key) {
        return null;
    }

    @Override
    public int order() {
        return 0;
    }
}
