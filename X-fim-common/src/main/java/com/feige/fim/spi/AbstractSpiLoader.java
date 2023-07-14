package com.feige.fim.spi;

import com.feige.api.annotation.Spi;
import com.feige.api.spi.SpiLoader;
import com.feige.api.spi.SpiNotFoundException;
import com.feige.fim.lg.Loggers;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractSpiLoader implements SpiLoader {

    protected static final Logger LOG = Loggers.LOADER;
    protected final Map<Class<?>, List<Object>> instanceMap = new ConcurrentHashMap<>();
    public static final Comparator<Object> SPI_ORDER = (o1, o2) -> {
        Spi spi1 = o1.getClass().getAnnotation(Spi.class);
        Spi spi2 = o2.getClass().getAnnotation(Spi.class);
        return Integer.compare(spi1.order(), spi2.order());
    };
    
    @Override
    public void register(Class<?> clazz, List<Object> instances) {
        this.instanceMap.put(clazz, instances);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) throws SpiNotFoundException {
        List<Object> spiList = loadClass(clazz);
        if (spiList != null && !spiList.isEmpty()){
            for (Object spi : spiList) {
                Spi spiAnn = spi.getClass().getAnnotation(Spi.class);
                String value;
                if (spiAnn != null){
                    value = spiAnn.value();
                }else {
                    value = spi.getClass().getName();
                }
                if (Objects.equals(value, key)){
                    return clazz.cast(spi);
                }
            }
            throw new SpiNotFoundException(clazz, key);
        }
        throw new SpiNotFoundException(clazz);
    }

    @Override
    public <T> T getFirst(Class<T> clazz) throws SpiNotFoundException {
        List<?> spiList = loadClass(clazz);
        if (CollectionUtils.isEmpty(spiList)){
            throw new SpiNotFoundException(clazz);
        }
        return clazz.cast(spiList.get(0));
    }

    @Override
    public <T> List<T> getAll(Class<T> clazz) throws SpiNotFoundException {
        List<?> spiList = loadClass(clazz);
        if (spiList == null){
            return Collections.emptyList();
        }
        return spiList.stream()
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkInstance(Object instance) {
        Spi spi = instance.getClass().getAnnotation(Spi.class);
        if (spi == null){
            LOG.warn("class = {}, No @Spi", instance.getClass().getName());
        }
        return spi != null;
    }

    protected  List<Object> loadClass(Class<?> clazz){
        List<Object> spiList = instanceMap.get(clazz);
        if (spiList == null || spiList.isEmpty()){
            load(clazz);
            spiList = instanceMap.get(clazz);
        }
        return spiList;
    }
}
