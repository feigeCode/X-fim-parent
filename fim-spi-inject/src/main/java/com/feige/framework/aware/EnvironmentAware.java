package com.feige.framework.aware;

import com.feige.framework.env.api.Environment;

public interface EnvironmentAware extends Aware {
    
    void setEnvironment(Environment environment);
}
