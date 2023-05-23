package com.feige.fim.spi;

import com.feige.api.spi.Spi;
import com.feige.api.spi.SpiLoader;
import com.feige.api.spi.SpiNotFoundException;

import java.util.List;

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
                    spiLoader = new JdkSpiLoader();
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
     * get object by config 
     * @param clazz class
     * @param configNullReturnPrimary Whether to take the first object whose primary is true when configured null
     * @return object
     * @param <T> class type
     * @throws SpiNotFoundException
     */
    public static <T extends Spi> T getByConfig(Class<T> clazz, boolean configNullReturnPrimary) throws SpiNotFoundException {
        return getSpiLoader().getByConfig(clazz, configNullReturnPrimary);
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

    public static void load(String className){
        getSpiLoader().load(className);
    }
    
}
