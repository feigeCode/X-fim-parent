package com.feige.framework.config;

import com.feige.framework.annotation.SpiComp;
import com.feige.framework.api.config.Config;
import com.feige.framework.api.config.ConfigFactory;
import com.feige.framework.utils.Configs;
import com.google.auto.service.AutoService;

import java.io.File;


@SpiComp("yaml")
@AutoService(ConfigFactory.class)
public class YamlConfigFactory implements ConfigFactory {
    @Override
    public Config create() throws IllegalStateException{
        YamlConfig yamlConfig = new YamlConfig();
        try {
            yamlConfig.parseFile(getFile());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return yamlConfig;
    }

    private File getFile() {
        String absolutePath = new File("").getAbsolutePath();
        String path = System.getProperty(Configs.CONFIG_FILE_KEY, Configs.DEFAULT_CONFIG_PATH);
        File file;
        if (Configs.DEFAULT_CONFIG_PATH.equals(path)){
            path = absolutePath + File.separator + path;
        }
        if (!path.endsWith("yaml") && !path.endsWith("yml")) {
            if (path.charAt(path.length() - 1) != '.') {
                path = path + ".";
            }
            File f = new File(path + "yaml");
            if (f.exists()) {
                path += "yaml";
            }else {
                path += "yml";
            }
        }
        file = new File(path);
        return file;
    }
    
    
}