package com.feige.fim.config.impl;

import com.feige.api.config.Config;
import com.feige.api.config.ConfigFactory;
import com.feige.fim.config.Configs;

import java.io.File;


public class YamlConfigFactory implements ConfigFactory {
    @Override
    public Config create() throws Exception{
        YamlConfig yamlConfig = new YamlConfig();
        yamlConfig.parseFile(getFile());
        return yamlConfig;
    }

    private File getFile() {
        String absolutePath = new File("").getAbsolutePath();
        String path = System.getProperty(Configs.CONFIG_FILE_KEY, Configs.DEFAULT_CONFIG_PATH);
        File file;
        if (Configs.DEFAULT_CONFIG_PATH.equals(path)){
            path = absolutePath + File.separator + path;
        }
        if (!path.endsWith(getKey()) && !path.endsWith("yml")) {
            if (path.charAt(path.length() - 1) != '.') {
                path = path + ".";
            }
            File f = new File(path + getKey());
            if (f.exists()) {
                path += getKey();
            }else {
                path += "yml";
            }
        }
        file = new File(path);
        return file;
    }


    @Override
    public String getKey() {
        return "yaml";
    }
    
}
