package com.feige.framework.env.api;


import com.feige.framework.context.api.Lifecycle;
import com.feige.framework.env.api.Config;
import com.feige.framework.env.api.ConfigFactory;

public interface Environment extends Lifecycle, Config {

     
     void setConfigFactory(ConfigFactory configFactory);
     ConfigFactory getConfigFactory();
     Config getSystemConfig();
     Config getAppConfig();
     Config getEnvConfig();
     Config getCompositeConfig();
}
