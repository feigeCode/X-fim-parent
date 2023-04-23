package com.feige.fim.spi;

import com.feige.api.config.Configs;
import com.feige.api.spi.Spi;
import com.feige.log.Logger;
import com.feige.log.Loggers;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class SpiLoader {
    private static final Logger LOG = Loggers.LOADER;
    private final Map<String, Map<String, Spi>> spiMap = new ConcurrentHashMap<>();

    private SpiLoader(){

    }

    public static SpiLoader getInstance(){
        return InnerSpiLoader.spiLoader;
    }

    public void register(String className, Spi spi){
        spiMap.computeIfAbsent(className,  k -> new ConcurrentHashMap<>()).put(spi.getKey(), spi);
    }

    public <T extends Spi> T get(String key, Class<T> clazz) throws SpiNotFoundException {
        Map<String, Spi> map = spiMap.get(clazz.getCanonicalName());
        if (map != null){
            Spi spi = map.get(key);
            if (spi != null){
                return clazz.cast(spi);
            }
            throw new SpiNotFoundException(clazz, key);
        }
        throw new SpiNotFoundException(clazz);
    }

    public <T extends Spi> T getSpiByConfig(Class<T> clazz) throws SpiNotFoundException {
        String name = clazz.getCanonicalName();
        String key = Configs.Spi.get(name);
        Map<String, Spi> map = spiMap.get(name);
        if (map != null){
            Spi spi = map.get(key);
            if (spi != null){
                return clazz.cast(spi);
            }
            throw new SpiNotFoundException(clazz, key);
        }
        throw new SpiNotFoundException(clazz);
    }


    public synchronized void load(String className) {
        try {
            Class<?> loadClass = Class.forName(className);
            Class<Spi> spiClass = Spi.class;
            if (spiClass.isAssignableFrom(loadClass)){
                LOG.warn("Must implement {}.", spiClass.getCanonicalName());
                return;
            }
            ServiceLoader<?> loader = ServiceLoader.load(loadClass);
            for (Object next : loader) {
                Spi spi = (Spi) next;
                register(className, spi);
            }
        }catch (Exception e){
            LOG.error("spi loader error:", e);
        }
    }

    private static class InnerSpiLoader {
        private static final SpiLoader spiLoader = new SpiLoader();
    }
}
