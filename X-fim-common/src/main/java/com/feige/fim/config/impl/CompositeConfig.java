package com.feige.fim.config.impl;

import com.feige.api.config.Config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author feige<br />
 * @ClassName: CompositeConfig <br/>
 * @Description: <br/>
 * @date: 2023/5/20 15:23<br/>
 */
public class CompositeConfig implements Config {
    
    private final List<Config> configList = new ArrayList<>();
    @Override
    public void parseConfig(Object config) throws Exception {
        
    }

    @Override
    public Integer getInt(String key, Integer defaultValue) {
        for (Config config : configList) {
            Integer value = config.getInt(key, defaultValue);
            if (value != null){
                return value;
            }
        }
        return defaultValue;
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        for (Config config : configList) {
            Long value = config.getLong(key, defaultValue);
            if (value != null){
                return value;
            }
        }
        return defaultValue;
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        for (Config config : configList) {
            Double value = config.getDouble(key, defaultValue);
            if (value != null){
                return value;
            }
        }
        return defaultValue;
    }

    @Override
    public String getString(String key, String defaultValue) {
        for (Config config : configList) {
            String value = config.getString(key, defaultValue);
            if (value != null){
                return value;
            }
        }
        return defaultValue;
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        for (Config config : configList) {
            Boolean value = config.getBoolean(key, defaultValue);
            if (value != null){
                return value;
            }
        }
        return defaultValue;
    }

    @Override
    public Map<String, Object> getMap(String key) {
        for (Config config : configList) {
            Map<String, Object> value = config.getMap(key);
            if (value != null){
                return value;
            }
        }
        return null;
    }

    @Override
    public List<String> getList(String key) {
        for (Config config : configList) {
            List<String> value = config.getList(key);
            if (value != null){
                return value;
            }
        }
        return null;
    }

    @Override
    public String[] getArr(String key) {
        for (Config config : configList) {
            String[] value = config.getArr(key);
            if (value != null){
                return value;
            }
        }
        return null;
    }

    @Override
    public Object getObject(String key) {
        for (Config config : configList) {
            Object value = config.getObject(key);
            if (value != null){
                return value;
            }
        }
        return null;
    }

    @Override
    public int order() {
        return 0;
    }
    
    public void addConfig(Config config){
        configList.add(config);
        configList.sort(Comparator.comparing(Config::order));
    }
}
