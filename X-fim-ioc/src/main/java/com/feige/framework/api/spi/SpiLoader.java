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
     * @throws NoSuchInstanceException
     */
    <T> T get(String key, Class<T> clazz) throws NoSuchInstanceException;


    /**
     * get first object 
     * @param clazz class
     * @return object
     * @param <T> class type
     * @throws NoSuchInstanceException
     */
    <T> T get(Class<T> clazz) throws NoSuchInstanceException;


    /**
     * get all object
     * @param clazz class
     * @return object list
     * @param <T> class type
     * @throws NoSuchInstanceException
     */
    <T> List<T> getByType(Class<T> clazz) throws NoSuchInstanceException;
    
    
}
