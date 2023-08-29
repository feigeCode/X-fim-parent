package com.feige.framework.api.context;


import com.feige.framework.api.config.Config;

public interface Environment extends  Lifecycle, Config {

     Config getSystemConfig();
     Config getAppConfig();
     Config getEnvConfig();
     Config getMemoryConfig();
     Config getCompositeConfig();
}
