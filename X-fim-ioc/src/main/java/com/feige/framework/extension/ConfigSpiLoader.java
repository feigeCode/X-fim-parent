package com.feige.framework.extension;



import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.order.OrderComparator;
import com.feige.framework.utils.SpiConfigsLoader;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigSpiLoader extends AbstractSpiLoader {

    public static final String TYPE = "config";

    public ConfigSpiLoader(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public <T> List<T>  doLoadSpiInstance(Class<T> loadClass) {
        try {
            List<T> objects = SpiConfigsLoader.loadConfigs(loadClass, this.getClass().getClassLoader());
            return objects.stream()
                    .filter(this::checkInstance)
                    .sorted(OrderComparator.getInstance())
                    .collect(Collectors.toList());
        }catch (Throwable e){
            LOG.error("spi loader error:", e);
        }
        return Collections.emptyList();
    }
    
    
}
