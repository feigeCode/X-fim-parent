package com.feige.fim.spi;

import com.feige.api.annotation.CacheOne;
import com.feige.api.constant.Const;
import com.feige.api.spi.Spi;
import com.feige.api.spi.SpiLoader;
import com.feige.api.spi.SpiNotFoundException;
import com.feige.fim.config.Configs;
import com.feige.fim.lg.Loggers;
import com.feige.fim.utils.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JdkSpiLoader implements SpiLoader {
    private static final Logger LOG = Loggers.LOADER;
    private final Map<Class<?>, List<Spi>> spiMap = new ConcurrentHashMap<>();
    private final Class<Spi> spiClass = Spi.class;

    

    @Override
    public void register(Class<?> clazz, List<Spi> objects) {
        if (objects.size() > 1){
            objects.sort(Comparator.comparing(Spi::order));
        }
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
    public <T extends Spi> T getByConfig(Class<T> clazz, boolean configNullReturnPrimary) throws SpiNotFoundException {
        String key = null;
        try {
            key = Configs.getString(clazz.getName());
        }catch (NullPointerException ignore){}
        List<Spi> spiList = spiMap.get(clazz);
        if (spiList != null && !spiList.isEmpty()){
            if (StringUtil.isNotBlank(key)) {
                for (Spi spi : spiList) {
                    if (Objects.equals(spi.getKey(), key)) {
                        return clazz.cast(spi);
                    }
                }
            }
            if (configNullReturnPrimary){
                for (Spi spi : spiList) {
                    if (spi.primary()){
                        return clazz.cast(spi);
                    }
                }
            }
            throw new SpiNotFoundException(clazz, key);
        }
        throw new SpiNotFoundException(clazz);
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
                        Set<String> keys = keys(className);
                        for (Object next : loader) {
                            Spi spi = (Spi) next;
                            spiList.add(spi);
                        }
                        if (keys != null && keys.size() > 0){
                            spiList = spiList.stream()
                                    .filter(spi -> keys.contains(spi.getKey()))
                                    .collect(Collectors.toList());
                        }else {
                            CacheOne cacheOne = loadClass.getAnnotation(CacheOne.class);
                            if (cacheOne != null){
                                List<Spi> collect = spiList.stream()
                                        .filter(Spi::primary)
                                        .collect(Collectors.toList());
                                if (CollectionUtils.isEmpty(collect) && spiList.size() > 0){
                                    spiList = spiList.subList(0, 1);
                                }else {
                                    spiList = collect.subList(0, 1);
                                }
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
    
    
    private Set<String> keys(String className){
        Map<String, Object> map = Configs.getMap(Configs.ConfigKey.SPI_LOADER_KEY);
        String key = MapUtils.getString(map, className);
        if (StringUtil.isNotBlank(key)){
            return new HashSet<>(Arrays.asList(key.split(Const.COMMA)));
        }
        return null;
    }
}
