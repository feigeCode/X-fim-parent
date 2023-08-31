package com.feige.framework.env;

import com.feige.framework.env.api.Config;

/**
 * @author feige<br />
 * @ClassName: SystemConfig <br/>
 * @Description: <br/>
 * @date: 2023/5/20 10:08<br/>
 */
public class SystemConfig implements Config {
  
    @Override
    public Object getObject(String key) {
        return System.getProperty(key);
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }
}
