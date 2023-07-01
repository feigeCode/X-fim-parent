package com.feige.fim.spi;

import com.feige.api.spi.Spi;
import com.feige.api.spi.SpiLoader;
import com.feige.api.spi.SpiNotFoundException;
import com.feige.fim.lg.Loggers;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractSpiLoader implements SpiLoader {

    protected static final Logger LOG = Loggers.LOADER;
    protected final Map<Class<?>, List<Spi>> spiMap = new ConcurrentHashMap<>();
    protected final Class<Spi> spiClass = Spi.class;
    
    @Override
    public void register(Class<?> clazz, List<Spi> objects) {
        this.spiMap.put(clazz, objects);
    }

    @Override
    public <T extends Spi> T get(String key, Class<T> clazz) throws SpiNotFoundException {
        List<Spi> spiList = loadClass(clazz);
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
    public <T extends Spi> T getFirst(Class<T> clazz) throws SpiNotFoundException {
        final List<Spi> spiList = loadClass(clazz);
        if (CollectionUtils.isEmpty(spiList)){
            throw new SpiNotFoundException(clazz);
        }
        return clazz.cast(spiList.get(0));
    }

    @Override
    public <T extends Spi> List<T> getAll(Class<T> clazz) throws SpiNotFoundException {
        List<Spi> spiList = loadClass(clazz);
        if (spiList == null){
            return Collections.emptyList();
        }
        return spiList.stream()
                .map(clazz::cast)
                .collect(Collectors.toList());
    }
    
    protected  List<Spi> loadClass(Class<?> clazz){
        List<Spi> spiList = spiMap.get(clazz);
        if (spiList == null || spiList.isEmpty()){
            load(clazz);
            spiList = spiMap.get(clazz);
        }
        return spiList;
    }
}
