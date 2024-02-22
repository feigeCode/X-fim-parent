package com.feige.framework.env.api;


import com.feige.framework.context.api.Lifecycle;

public interface Environment extends Lifecycle, Config {

     
     void setConfigFactory(ConfigFactory configFactory);
     ConfigFactory getConfigFactory();
     Config getSystemConfig();
     Config getAppConfig();
     Config getEnvConfig();
     Config getCompositeConfig();
}
