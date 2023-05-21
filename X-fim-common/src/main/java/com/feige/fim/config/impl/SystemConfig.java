package com.feige.fim.config.impl;

import com.feige.api.config.Config;

/**
 * @author feige<br />
 * @ClassName: SystemConfig <br/>
 * @Description: <br/>
 * @date: 2023/5/20 10:08<br/>
 */
public class SystemConfig implements Config {
    @Override
    public void parseConfig(Object obj) throws Exception {
        
    }
    @Override
    public Object getObject(String key) {
        return System.getProperty(key);
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }
}
