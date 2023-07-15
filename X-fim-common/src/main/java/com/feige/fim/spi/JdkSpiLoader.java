package com.feige.fim.spi;


import com.feige.api.order.OrderComparator;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class JdkSpiLoader extends AbstractSpiLoader {
    
    public static final String TYPE = "jdk";


    @Override
    public void doLoadInstance(Class<?> loadClass) {
        try {
            List<Object> list = instanceCache.get(loadClass);
            if (list == null){
                String className = loadClass.getName();
                synchronized (className.intern()){
                    list = instanceCache.get(loadClass);
                    if (list == null){
                        ServiceLoader<?> loader = ServiceLoader.load(loadClass);
                        List<Object> instanceList = new ArrayList<>();
                        for (Object next : loader) {
                            if (this.checkInstance(next)){
                                instanceList.add(applyBeanPostProcessorsBeforeInitialization(next, getInstanceName(next.getClass())));
                            }
                        }
                        if (instanceList.size() > 1){
                            instanceList.sort(OrderComparator.getInstance());
                        }
                        if (CollectionUtils.isNotEmpty(instanceList)){
                            register(loadClass, instanceList);
                        }else {
                            LOG.warn("class = {}, No implementation classes have been registered", className);
                        }
                    }
                }
            }
        }catch (Exception e){
            LOG.error("spi loader error:", e);
        }
    }
}
