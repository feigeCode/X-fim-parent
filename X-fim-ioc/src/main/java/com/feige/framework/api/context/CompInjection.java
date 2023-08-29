package com.feige.framework.api.context;


public interface CompInjection extends ApplicationContextAware, Lifecycle{
    
    
    void inject(Object comp);
}
