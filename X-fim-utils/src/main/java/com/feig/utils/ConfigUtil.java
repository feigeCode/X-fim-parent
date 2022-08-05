package com.feig.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

public final class ConfigUtil {

    public static final String CONFIG_FILE_KEY = "fim.path";

    public static final Config CONFIG = loadConfig();

    public static Config loadConfig(){
        Config config = ConfigFactory.load();
        if (config.hasPath(CONFIG_FILE_KEY)){
            File file = new File(config.getString(CONFIG_FILE_KEY));
            if (file.exists()) {
                Config custom = ConfigFactory.parseFile(file);
                config = custom.withFallback(config);
            }
        }
        return config;
    }
}
