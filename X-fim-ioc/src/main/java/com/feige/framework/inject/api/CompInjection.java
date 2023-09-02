package com.feige.framework.inject.api;


import com.feige.framework.aware.ApplicationContextAware;
import com.feige.framework.context.api.Lifecycle;

public interface CompInjection extends ApplicationContextAware, Lifecycle {
    
    
    void inject(Object comp);
}
