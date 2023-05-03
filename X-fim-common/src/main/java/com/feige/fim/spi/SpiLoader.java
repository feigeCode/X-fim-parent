package com.feige.fim.spi;

import com.feige.fim.utils.StringUtil;
import com.feige.api.annotation.LoadOnlyTheFirstOne;
import com.feige.fim.config.Configs;
import com.feige.api.spi.Spi;
import org.slf4j.Logger;
import com.feige.fim.lg.Loggers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class SpiLoader {
    private static final Logger LOG = Loggers.LOADER;
    private final Map<String, List<Spi>> spiMap = new ConcurrentHashMap<>();
    private final Class<Spi> spiClass = Spi.class;

    private SpiLoader(){

    }

    public static SpiLoader getInstance(){
        return InnerSpiLoader.spiLoader;
    }

    public void register(String className, List<Spi> spiList){
        if (spiList.size() > 1){
            spiList.sort(Comparator.comparing(Spi::order));
        }
        this.spiMap.put(className, spiList);
    }

    public <T extends Spi> T get(String key, Class<T> clazz) throws SpiNotFoundException {
        List<Spi> spiList = spiMap.get(clazz.getCanonicalName());
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

    /**
     * The value is obtained through the configuration first.
     * If the value is not configured or cannot be obtained, the value of primary is true
     * @param clazz class
     * @param <T> class type
     * @return instance
     * @throws SpiNotFoundException
     */
    public <T extends Spi> T getSpiByConfigOrPrimary(Class<T> clazz) throws SpiNotFoundException {
        String name = clazz.getCanonicalName();
        String key = null;
        try {
            key = Configs.getString(name);
        }catch (NullPointerException ignore){}
        List<Spi> spiList = spiMap.get(name);
        if (spiList != null && !spiList.isEmpty()){
            if (StringUtil.isNotBlank(key)) {
                for (Spi spi : spiList) {
                    if (Objects.equals(spi.getKey(), key)) {
                        return clazz.cast(spi);
                    }
                }
            }
            for (Spi spi : spiList) {
                if (spi.isPrimary()){
                    return clazz.cast(spi);
                }
            }
            throw new SpiNotFoundException(clazz, key);
        }
        throw new SpiNotFoundException(clazz);
    }

    public  <T extends Spi> T getDefault(Class<T> clazz) throws SpiNotFoundException {
        String name = clazz.getCanonicalName();
        List<Spi> spiList = spiMap.get(name);
        if (spiList != null && !spiList.isEmpty()){
            for (Spi spi : spiList) {
                if (spi.isPrimary()){
                    return clazz.cast(spi);
                }
            }
        }
        throw new SpiNotFoundException(clazz);
    }

    public synchronized void load(String className) {
        try {
            Class<?> loadClass = Class.forName(className);
            if (!spiClass.isAssignableFrom(loadClass)){
                LOG.warn("Must implement {}.", spiClass.getCanonicalName());
                return;
            }
            ServiceLoader<?> loader = ServiceLoader.load(loadClass);
            ArrayList<Spi> spiList = new ArrayList<>();
            LoadOnlyTheFirstOne loadOnlyTheFirstOne = loadClass.getAnnotation(LoadOnlyTheFirstOne.class);
            for (Object next : loader) {
                Spi spi = (Spi) next;
                spiList.add(spi);
                if(loadOnlyTheFirstOne != null){
                    break;
                }
            }
            register(className, spiList);
        }catch (Exception e){
            LOG.error("spi loader error:", e);
        }
    }


    private static class InnerSpiLoader {
        private static final SpiLoader spiLoader = new SpiLoader();
    }
}
