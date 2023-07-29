package com.feige.framework.api.context;

public interface EnvironmentAware extends Aware {
    
    void setEnvironment(Environment environment);
}
