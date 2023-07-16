package com.feige.fim.context;

import com.feige.api.spi.SpiLoader;
import com.feige.api.spi.SpiNotFoundException;
import com.feige.fim.config.Configs;
import com.feige.fim.lg.Loggers;
import com.feige.fim.spi.ConfigSpiLoader;
import com.feige.fim.spi.JdkSpiLoader;

import java.util.List;
import java.util.Objects;

/**
 * @author feige<br />
 * @ClassName: AppContext <br/>
 * @Description: <br/>
 * @date: 2023/5/20 15:13<br/>
 */
public class AppContext {

    
    private static volatile SpiLoader spiLoader;
    
    public static SpiLoader getSpiLoader(){
        if (spiLoader == null){
            synchronized (AppContext.class){
                if (spiLoader == null){
                    String type = Configs.getString(Configs.ConfigKey.SPI_LOADER_TYPE);
                    if (Objects.equals(type, ConfigSpiLoader.TYPE)){
                        spiLoader = new ConfigSpiLoader();
                        Loggers.LOADER.info("使用" + ConfigSpiLoader.class.getName() + "加载器");
                    }else {
                        spiLoader = new JdkSpiLoader();
                        Loggers.LOADER.info("使用" + JdkSpiLoader.class.getName() + "加载器");
                    }
                    spiLoader.initialize();
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
    public static <T> T get(String key, Class<T> clazz) throws SpiNotFoundException {
        return getSpiLoader().get(key, clazz);
    }


    /**
     * get first object
     * @param clazz class
     * @return object
     * @param <T> class type
     * @throws SpiNotFoundException
     */
    public static <T> T getFirst(Class<T> clazz) throws SpiNotFoundException {
        return getSpiLoader().getFirst(clazz);
    }


    /**
     * get all object
     * @param clazz class
     * @return object list
     * @param <T> class type
     * @throws SpiNotFoundException
     */
    public static <T> List<T> getAll(Class<T> clazz) throws SpiNotFoundException {
        return getSpiLoader().getAll(clazz);
    }
}
