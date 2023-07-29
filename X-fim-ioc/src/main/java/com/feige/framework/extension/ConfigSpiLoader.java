package com.feige.framework.extension;



import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.Environment;
import com.feige.framework.utils.SpiConfigsLoader;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigSpiLoader extends AbstractSpiLoader {

    public static final String TYPE = "config";

    public ConfigSpiLoader(ApplicationContext applicationContext, Environment environment) {
        super(applicationContext, environment);
    }

    @Override
    public List<Object>  doLoadInstance(Class<?> loadClass) {
        try {
            List<?> objects = SpiConfigsLoader.loadConfigs(loadClass, this.getClass().getClassLoader());
            return objects.stream()
                    .filter(this::checkInstance)
                    .map(o -> (Object) o)
                    .collect(Collectors.toList());
        }catch (Throwable e){
            LOG.error("spi loader error:", e);
        }
        return Collections.emptyList();
    }
    
    
}
