package com.feige.framework.context;

import com.feige.framework.utils.Configs.ConfigKey;


public class StandardEnvironment extends AbstractEnvironment {
    @Override
    public void initialize() throws IllegalStateException {
        super.initialize();
        initLogConfig();
    }

    public void initLogConfig() {
        System.setProperty("log.home", this.getString(ConfigKey.LOG_DIR));
        System.setProperty("log.root.level", this.getString(ConfigKey.LOG_LEVEL));
        System.setProperty("logback.configurationFile", this.getString(ConfigKey.LOG_CONF_PATH));
    }
    
}
