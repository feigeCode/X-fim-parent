package com.feige.api.spi;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: SpiLoader <br/>
 * @Description: <br/>
 * @date: 2023/5/20 14:22<br/>
 */
public interface SpiLoader {

    /**
     * register objects
     * @param clazz class
     * @param objects object list
     */
    void register(Class<?> clazz, List<Spi> objects);


    /**
     * get object by key
     * @param key key
     * @param clazz class
     * @return Object
     * @param <T> class type
     * @throws SpiNotFoundException
     */
    <T extends Spi> T get(String key, Class<T> clazz) throws SpiNotFoundException;


    /**
     * get object by config 
     * @param clazz class
     * @param configNullReturnPrimary Whether to take the first object whose primary is true when configured null
     * @return object
     * @param <T> class type
     * @throws SpiNotFoundException
     */
    <T extends Spi> T getByConfig(Class<T> clazz, boolean configNullReturnPrimary) throws SpiNotFoundException;


    /**
     * get all object
     * @param clazz class
     * @return object list
     * @param <T> class type
     * @throws SpiNotFoundException
     */
    <T extends Spi> List<T> getAll(Class<T> clazz) throws SpiNotFoundException;


    /**
     * load class
     * @param className class name
     */
    void load(String className);
    
}
