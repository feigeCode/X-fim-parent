package com.feige.framework.aware;

import com.feige.framework.annotation.DisableInject;
import com.feige.framework.env.api.Environment;

public interface EnvironmentAware extends Aware {

    @DisableInject
    
    void setEnvironment(Environment environment);
}
