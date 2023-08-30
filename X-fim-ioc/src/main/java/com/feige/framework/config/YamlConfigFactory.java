package com.feige.framework.config;

import com.feige.utils.common.YamlUtils;
import com.feige.framework.api.config.Config;
import com.feige.framework.api.config.ConfigFactory;
import com.feige.framework.utils.Configs;
import com.feige.utils.spi.annotation.SpiComp;
import com.google.auto.service.AutoService;


import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;


@SpiComp(interfaces = ConfigFactory.class)
public class YamlConfigFactory implements ConfigFactory {
    @Override
    public Config create(File file) throws IllegalStateException{
        try {
            InputStream is = Files.newInputStream(file.toPath());
            Map<String, Object> parser = YamlUtils.parser(is);
            return new MapConfig(parser);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
}
