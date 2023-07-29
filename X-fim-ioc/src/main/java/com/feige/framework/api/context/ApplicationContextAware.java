package com.feige.framework.api.context;

public interface ApplicationContextAware extends Aware{
    
    void setApplicationContext(ApplicationContext applicationContext);
}
