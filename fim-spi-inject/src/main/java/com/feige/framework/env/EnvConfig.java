package com.feige.framework.env;

import com.feige.framework.env.api.Config;

import java.util.Map;

/**
 * @author feige<br />
 * @ClassName: EnvConfig <br/>
 * @Description: <br/>
 * @date: 2023/5/20 15:24<br/>
 */
public class EnvConfig implements Config {
    

    @Override
    public Object getObject(String key) {
        return System.getenv(key);
    }

    @Override
    public Map<String, Object> getMapByKeyPrefix(String key) {
        Map<String, String> envMap = System.getenv();
        return convertMap(envMap, key);
    }

    @Override
    public int order() {
        return 0;
    }
}
