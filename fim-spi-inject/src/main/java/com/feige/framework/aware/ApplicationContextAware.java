package com.feige.framework.aware;

import com.feige.framework.context.api.ApplicationContext;

public interface ApplicationContextAware extends Aware {
    
    void setApplicationContext(ApplicationContext applicationContext);
}
