package com.feige.fim.spi;

import com.feige.api.annotation.CacheOne;
import com.feige.api.spi.Spi;
import com.feige.api.spi.SpiLoader;
import com.feige.api.spi.SpiNotFoundException;
import com.feige.fim.config.Configs;
import com.feige.fim.lg.Loggers;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JdkSpiLoader implements SpiLoader {
    private static final Logger LOG = Loggers.LOADER;
    private final Map<Class<?>, List<Spi>> spiMap = new ConcurrentHashMap<>();
    private final Class<Spi> spiClass = Spi.class;

    

    @Override
    public void register(Class<?> clazz, List<Spi> objects) {
        this.spiMap.put(clazz, objects);
    }

    @Override
    public <T extends Spi> T get(String key, Class<T> clazz) throws SpiNotFoundException {
        List<Spi> spiList = spiMap.get(clazz);
        if (spiList != null && !spiList.isEmpty()){
            for (Spi spi : spiList) {
                if (Objects.equals(spi.getKey(), key)){
                    return clazz.cast(spi);
                }
            }
            throw new SpiNotFoundException(clazz, key);
        }
        throw new SpiNotFoundException(clazz);
    }

    @Override
    public <T extends Spi> T getByConfig(Class<T> clazz) throws SpiNotFoundException {
        List<String> keys = Configs.getSpiConfig().get(clazz.getName());
        if (CollectionUtils.isEmpty(keys)){
            throw new SpiNotFoundException(clazz);
        }
        return get(keys.get(0), clazz);
    }

    @Override
    public <T extends Spi> List<T> getAll(Class<T> clazz) throws SpiNotFoundException {
        List<Spi> spiList = spiMap.get(clazz);
        return spiList.stream()
                .map(clazz::cast)
                .collect(Collectors.toList());
    }


    @Override
    public void load(String className) {
        try {
            Class<?> loadClass = Class.forName(className);
            if (!spiClass.isAssignableFrom(loadClass)){
                LOG.warn("Must implement {}.", spiClass.getName());
                return;
            }
            List<Spi> list = spiMap.get(loadClass);
            if (list == null){
                synchronized (className.intern()){
                    list = spiMap.get(loadClass);
                    if (list == null){
                        ServiceLoader<?> loader = ServiceLoader.load(loadClass);
                        List<Spi> spiList = new ArrayList<>();
                        List<String> keys = keys(className);
                        for (Object next : loader) {
                            Spi spi = (Spi) next;
                            spiList.add(spi);
                        }
                        if (spiList.size() > 1){
                            spiList.sort(Comparator.comparing(Spi::order));
                        }
                        if (keys != null && keys.size() > 0){
                            spiList = spiList.stream()
                                    .filter(spi -> keys.contains(spi.getKey()))
                                    .collect(Collectors.toList());
                        }else {
                            CacheOne cacheOne = loadClass.getAnnotation(CacheOne.class);
                            if (cacheOne == null){
                                List<String> keys_ = spiList.stream()
                                        .map(Spi::getKey)
                                        .collect(Collectors.toList());
                                Configs.getSpiConfig().put(className, keys_);
                            }else {
                                spiList = Collections.singletonList(spiList.get(0));
                                Configs.getSpiConfig().put(className, Collections.singletonList(spiList.get(0).getKey()));
                            }
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
    
    
    private List<String> keys(String className){
        return Configs.getSpiConfig().get(className);
    }
}
