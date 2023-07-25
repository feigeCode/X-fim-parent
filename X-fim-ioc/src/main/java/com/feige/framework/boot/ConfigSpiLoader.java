package com.feige.framework.boot;



import com.feige.framework.utils.SpiConfigsLoader;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigSpiLoader extends AbstractSpiLoader {

    public static final String TYPE = "config";
    
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
