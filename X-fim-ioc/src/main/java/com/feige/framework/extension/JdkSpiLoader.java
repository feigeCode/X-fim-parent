package com.feige.framework.extension;


import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.Environment;
import com.feige.framework.order.OrderComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class JdkSpiLoader extends AbstractSpiLoader {
    
    public static final String TYPE = "jdk";

    public JdkSpiLoader(ApplicationContext applicationContext, Environment environment) {
        super(applicationContext, environment);
    }


    @Override
    public List<Object> doLoadInstance(Class<?> loadClass) {
        List<Object> instanceList = new ArrayList<>();
        try {
            ServiceLoader<?> loader = ServiceLoader.load(loadClass);
            for (Object next : loader) {
                if (this.checkInstance(next)){
                    instanceList.add(next);
                }
            }
            if (instanceList.size() > 1){
                instanceList.sort(OrderComparator.getInstance());
            }
        }catch (Exception e){
            LOG.error("spi loader error:", e);
        }
        return instanceList;
    }
}
