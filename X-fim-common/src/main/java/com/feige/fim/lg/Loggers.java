package com.feige.fim.lg;

import com.feige.fim.config.Configs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Loggers {
    
    static void init() {
        System.setProperty("log.home", Configs.getString(Configs.ConfigKey.LOG_DIR));
        System.setProperty("log.root.level", Configs.getString(Configs.ConfigKey.LOG_LEVEL));
        System.setProperty("logback.configurationFile", Configs.getString(Configs.ConfigKey.LOG_CONF_PATH));
    }

    Logger CONSOLE = LoggerFactory.getLogger("console");
    Logger SRD = LoggerFactory.getLogger("srd.log");
    Logger TASK = LoggerFactory.getLogger("task.log");
    Logger SERVER = LoggerFactory.getLogger("server.log");
    Logger CODEC = LoggerFactory.getLogger("codec.log");
    Logger LOADER = LoggerFactory.getLogger("loader.log");
    Logger UTILS = LoggerFactory.getLogger("utils.log");
    Logger HEARTBEAT = LoggerFactory.getLogger("heartbeat.log");

}
