package com.feige.framework.env;

import com.feige.framework.env.api.Config;
import com.feige.utils.common.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
    public Map<String, Object> getMapByKeyPrefix(String key) {
        Map<String, Object> result = new HashMap<>();
        for (Config config : configList) {
            Map<String, Object> map = config.getMapByKeyPrefix(key);
            if (map != null){
                result.putAll(map);
            }
        }
        return result;
    }

    @Override
    public Collection<String> getCollection(String key) {
        for (Config config : configList) {
            Collection<String> value = config.getCollection(key);
            if (value != null){
                return value;
            }
        }
        return Collections.emptyList();
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
