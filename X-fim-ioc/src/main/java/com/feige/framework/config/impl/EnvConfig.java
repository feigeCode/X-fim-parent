package com.feige.framework.config.impl;

import com.feige.api.config.Config;

/**
 * @author feige<br />
 * @ClassName: EnvConfig <br/>
 * @Description: <br/>
 * @date: 2023/5/20 15:24<br/>
 */
public class EnvConfig implements Config {
    
    @Override
    public void parseConfig(Object config) throws Exception {
        
    }

    @Override
    public Object getObject(String key) {
        return System.getenv(key);
    }

    @Override
    public int order() {
        return 0;
    }
}
