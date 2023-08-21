package com.feige.framework.config;

import com.feige.framework.api.config.Config;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapConfig implements Config {
    protected final Map<String, Object> config;

    public MapConfig(Map<String, Object> config) {
        this.config = config;
    }

    @Override
    public Integer getInt(String key, Integer defaultValue) {
        return MapUtils.getInteger(this.config, key, defaultValue);
    }
    

    @Override
    public Long getLong(String key, Long defaultValue) {
        return MapUtils.getLong(this.config, key, defaultValue);
    }
    

    @Override
    public Double getDouble(String key, Double defaultValue) {
        return MapUtils.getDouble(this.config, key, defaultValue);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return MapUtils.getString(this.config, key, defaultValue);
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return MapUtils.getBoolean(this.config, key, defaultValue);
    }

    @Override
    public Map<String, Object> getMap(String key) {
        Map<String, Object> map = (Map<String, Object>) MapUtils.getMap(this.config, key);
        if (map == null){
            map = new HashMap<>();
            for (Map.Entry<String, Object> entry : this.config.entrySet()) {
                String entryKey = entry.getKey();
                Object value = entry.getValue();
                if (entryKey.startsWith(key + ".")){
                    map.put(entryKey.substring(key.length() + 1), value);
                }
            }
        }
        return map;
    }

    @Override
    public List<String> getList(String key) {
        return this.getObject(key, List.class);
    }

    @Override
    public String[] getArr(String key) {
        return this.getObject(key, String[].class);
    }

    @Override
    public Object getObject(String key) {
        return MapUtils.getObject(this.config, key);
    }

    @Override
    public int order() {
        return 2;
    }
}
