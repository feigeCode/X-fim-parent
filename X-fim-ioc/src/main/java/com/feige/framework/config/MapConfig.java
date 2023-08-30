package com.feige.framework.config;

import com.feige.framework.api.config.Config;
import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapConfig implements Config {
    protected final Map<String, Object> config;
    protected final Map<String, Object> memoryMapConfig = new ConcurrentHashMap<>();

    public MapConfig(Map<String, Object> config) {
        this.config = config;
    }

    @Override
    public Map<String, Object> getMapByKeyPrefix(String key) {
        Map<String, Object> result = convertMap(new HashMap<>(config), key);
        result.putAll(convertMap(new HashMap<>(memoryMapConfig), key));
        return result;
    }

    @Override
    public Collection<String> getCollection(String key) {
        Object val = this.memoryMapConfig.get(key);
        if (val == null){
            val = this.config.get(key);
        }
        return (Collection<String>) val;
    }

    @Override
    public Object getObject(String key) {
        Object val = MapUtils.getObject(this.memoryMapConfig, key);
        if (val == null){
            val = MapUtils.getObject(this.config, key);
        }
        return val;
    }

    @Override
    public void setConfig(String key, Object value) {
        this.memoryMapConfig.put(key, value);
    }

    @Override
    public int order() {
        return 2;
    }
}
