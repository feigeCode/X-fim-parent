package com.feige.fim.spi;

import com.feige.api.spi.Spi;
import com.feige.api.spi.SpiLoader;
import com.feige.api.spi.SpiNotFoundException;
import com.feige.fim.config.Configs;
import com.feige.fim.lg.Loggers;

import java.util.List;
import java.util.Objects;

/**
 * @author feige<br />
 * @ClassName: SpiLoaderUtils <br/>
 * @Description: <br/>
 * @date: 2023/5/20 15:13<br/>
 */
public class SpiLoaderUtils {

    
    private static volatile SpiLoader spiLoader;
    
    public static SpiLoader getSpiLoader(){
        if (spiLoader == null){
            synchronized (SpiLoaderUtils.class){
                if (spiLoader == null){
                    String type = Configs.getString(Configs.ConfigKey.SPI_LOADER_TYPE);
                    if (Objects.equals(type, ConfigSpiLoader.TYPE)){
                        spiLoader = new ConfigSpiLoader();
                        Loggers.LOADER.info("使用" + ConfigSpiLoader.class.getName() + "加载器");
                    }else {
                        spiLoader = new JdkSpiLoader();
                        Loggers.LOADER.info("使用" + JdkSpiLoader.class.getName() + "加载器");
                    }
                }
            }
        }
        return spiLoader;
    }

    public static void setSpiLoader(SpiLoader loader){
        spiLoader = loader;
    }
    
    /**
     * get object by key
     * @param key key
     * @param clazz class
     * @return Object
     * @param <T> class type
     * @throws SpiNotFoundException
     */
    public static <T extends Spi> T get(String key, Class<T> clazz) throws SpiNotFoundException {
        return getSpiLoader().get(key, clazz);
    }


    /**
     * get first object
     * @param clazz class
     * @return object
     * @param <T> class type
     * @throws SpiNotFoundException
     */
    public static <T extends Spi> T getFirst(Class<T> clazz) throws SpiNotFoundException {
        return getSpiLoader().getFirst(clazz);
    }


    /**
     * get all object
     * @param clazz class
     * @return object list
     * @param <T> class type
     * @throws SpiNotFoundException
     */
    public static <T extends Spi> List<T> getAll(Class<T> clazz) throws SpiNotFoundException {
        return getSpiLoader().getAll(clazz);
    }

    public static void load(String className) throws ClassNotFoundException {
        Class<?> loadClass = Class.forName(className);
        load(loadClass);
    }

    public static void load(Class<?> loadClass){
        getSpiLoader().load(loadClass);
    }

    public static void initSpiLoader() {
        
    }
}
