package com.feige.framework.config;

import java.util.Map;

public class MemoryMapConfig extends MapConfig {
    public MemoryMapConfig(Map<String, Object> config) {
        super(config);
    }


    @Override
    public int order() {
        return super.order() - 1;
    }
    
    
    @Override
    public void setConfig(String key, Object value){
        this.config.put(key, value);
    }
}
