package com.feige.framework.boot;



import com.feige.fim.utils.AssertUtil;
import com.feige.fim.utils.ClassUtils;
import com.feige.fim.utils.ReflectionUtils;
import com.feige.framework.order.OrderComparator;
import com.feige.framework.config.Configs;
import com.feige.framework.utils.SpiConfigsLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ConfigSpiLoader extends AbstractSpiLoader {

    public static final String TYPE = "config";





    @Override
    public List<Object>  doLoadInstance(Class<?> loadClass) {
        try {
            List<?> objects = SpiConfigsLoader.loadConfigs(loadClass, this.getClass().getClassLoader());
            return objects.stream()
                    .map(o -> (Object) o)
                    .collect(Collectors.toList());
        }catch (Throwable e){
            LOG.error("spi loader error:", e);
        }
        return Collections.emptyList();
    }
    
    
}
