package com.feige.fim.config.impl;

import com.feige.api.config.Config;
import com.feige.fim.utils.StringUtils;

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
    public Object getObject(String key) {
        for (Config config : configList) {
            Object value = config.getObject(key);
            if (!StringUtils.isBlank(value)){
                return value;
            }
        }
        return null;
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
    public int order() {
        return 0;
    }
    
    public void addConfig(Config config){
        configList.add(config);
        configList.sort(Comparator.comparing(Config::order));
    }
}
