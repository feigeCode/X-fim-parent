package com.feige.framework.config;

import com.feige.framework.api.config.Config;

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
    public int order() {
        return 0;
    }
}
