package com.feige.framework.env;

import com.feige.utils.common.YamlUtils;
import com.feige.framework.env.api.Config;
import com.feige.framework.env.api.ConfigFactory;
import com.feige.utils.spi.annotation.SpiComp;


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
