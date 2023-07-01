package com.feige.fim.spi;


import com.feige.api.spi.Spi;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

public class JdkSpiLoader extends AbstractSpiLoader {
    
    public static final String TYPE = "jdk";


    @Override
    public void load(Class<?> loadClass) {
        try {
            if (!spiClass.isAssignableFrom(loadClass)){
                LOG.warn("Must implement {}.", spiClass.getName());
                return;
            }
            List<Spi> list = spiMap.get(loadClass);
            if (list == null){
                String className = loadClass.getName();
                synchronized (className.intern()){
                    list = spiMap.get(loadClass);
                    if (list == null){
                        ServiceLoader<?> loader = ServiceLoader.load(loadClass);
                        List<Spi> spiList = new ArrayList<>();
                        for (Object next : loader) {
                            Spi spi = (Spi) next;
                            spiList.add(spi);
                        }
                        if (spiList.size() > 1){
                            spiList.sort(Comparator.comparing(Spi::order));
                        }
                        if (CollectionUtils.isNotEmpty(spiList)){
                            register(loadClass, spiList);
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
