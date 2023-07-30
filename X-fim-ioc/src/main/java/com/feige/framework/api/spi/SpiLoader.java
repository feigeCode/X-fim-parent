package com.feige.framework.api.spi;

import com.feige.framework.api.context.Lifecycle;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: SpiLoader <br/>
 * @Description: <br/>
 * @date: 2023/5/20 14:22<br/>
 */
public interface SpiLoader extends Lifecycle {

    /**
     * register object
     * @param instanceName instance name
     * @param instance object 
     */
    void register(String instanceName, Object instance);


    /**
     * get object by key
     * @param key key
     * @param clazz class
     * @return Object
     * @param <T> class type
     * @throws SpiNotFoundException
     */
    <T> T get(String key, Class<T> clazz) throws SpiNotFoundException;


    /**
     * get first object 
     * @param clazz class
     * @return object
     * @param <T> class type
     * @throws SpiNotFoundException
     */
    <T> T getFirst(Class<T> clazz) throws SpiNotFoundException;


    /**
     * get all object
     * @param clazz class
     * @return object list
     * @param <T> class type
     * @throws SpiNotFoundException
     */
    <T> List<T> getAll(Class<T> clazz) throws SpiNotFoundException;
    
    
}
