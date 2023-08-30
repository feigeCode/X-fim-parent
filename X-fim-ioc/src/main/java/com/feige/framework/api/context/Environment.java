package com.feige.framework.api.context;


import com.feige.framework.api.config.Config;
import com.feige.framework.api.config.ConfigFactory;

public interface Environment extends  Lifecycle, Config {

     
     void setConfigFactory(ConfigFactory configFactory);
     ConfigFactory getConfigFactory();
     Config getSystemConfig();
     Config getAppConfig();
     Config getEnvConfig();
     Config getCompositeConfig();
}
