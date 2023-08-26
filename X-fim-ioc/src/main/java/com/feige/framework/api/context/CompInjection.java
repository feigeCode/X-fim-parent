package com.feige.framework.api.context;

public interface CompInjection extends ApplicationContextAware{
    
    
    void inject(Object comp);
}
