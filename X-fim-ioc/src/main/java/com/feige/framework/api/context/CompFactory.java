package com.feige.framework.api.context;

import com.feige.framework.api.spi.NoSuchInstanceException;

import java.util.List;

public interface CompFactory extends Lifecycle {
    /**
     * register object
     * @param instanceName instance name
     * @param instance object 
     */
    void register(String instanceName, Object instance);


    /**
     * get object by compName
     * @param compName compName
     * @param requireType class
     * @param args arguments
     * @return Object
     * @param <T> class type
     * @throws NoSuchInstanceException
     */
    <T> T get(String compName, Class<T> requireType, Object... args) throws NoSuchInstanceException;


    /**
     * get first object 
     * @param requireType class
     * @param args arguments                    
     * @return object
     * @param <T> class type
     * @throws NoSuchInstanceException
     */
    <T> T get(Class<T> requireType, Object... args) throws NoSuchInstanceException;


    /**
     * get all object
     * @param requireType class
     * @return object list
     * @param <T> class type
     * @throws NoSuchInstanceException
     */
    <T> List<T> getByType(Class<T> requireType) throws NoSuchInstanceException;
    
    
    boolean isSupported(Class<?> type);
}
