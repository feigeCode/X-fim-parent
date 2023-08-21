package com.feige.framework.extension;


import com.feige.framework.api.context.ApplicationContext;
import com.feige.utils.order.OrderComparator;
import com.feige.utils.spi.ServicesLoader;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JdkSpiLoader extends AbstractSpiLoader {
    
    public static final String TYPE = "jdk";

    public JdkSpiLoader(ApplicationContext applicationContext) {
        super(applicationContext);
    }


    @Override
    public <T> List<T> doLoadSpiInstance(Class<T> loadClass) {
        try {
            List<String> list = ServicesLoader.loadServices(loadClass, this.getClass().getClassLoader());
            return list.stream()
                    .map(this::<T>createInstance)
                    .filter(this::checkInstance)
                    .sorted(OrderComparator.getInstance())
                    .collect(Collectors.toList());
        }catch (Throwable e){
            LOG.error("spi loader error:", e);
        }
        return Collections.emptyList();
    }
}
