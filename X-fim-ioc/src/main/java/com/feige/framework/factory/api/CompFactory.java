package com.feige.framework.factory.api;


import com.feige.framework.context.api.Lifecycle;
import com.feige.framework.spi.api.NoSuchInstanceException;

import java.util.List;


public interface CompFactory extends Lifecycle {
    
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
    
    
    
    boolean isGlobal(Class<?> type, String compName);
    
    
    boolean isModule(Class<?> type, String compName);
    
    
    boolean isOne(Class<?> type, String compName);
    
}
