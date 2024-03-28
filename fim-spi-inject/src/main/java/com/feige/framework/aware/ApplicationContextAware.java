package com.feige.framework.aware;

import com.feige.framework.annotation.DisableInject;
import com.feige.framework.context.api.ApplicationContext;

public interface ApplicationContextAware extends Aware {


    @DisableInject
    void setApplicationContext(ApplicationContext applicationContext);
}
