package com.feige.framework.utils;

import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.spi.SpiNotFoundException;


import java.util.List;


/**
 * @author feige<br />
 * @ClassName: AppContext <br/>
 * @Description: <br/>
 * @date: 2023/5/20 15:13<br/>
 */
public class AppContext  {

    
    private static ApplicationContext applicationContext;
    
    

    public static void setApplication(ApplicationContext applicationContext){
        AppContext.applicationContext = applicationContext;
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
        return applicationContext.get(key, clazz);
    }


    /**
     * get first object
     * @param clazz class
     * @return object
     * @param <T> class type
     * @throws SpiNotFoundException
     */
    public static <T> T getFirst(Class<T> clazz) throws SpiNotFoundException {
        return applicationContext.getFirst(clazz);
    }


    /**
     * get all object
     * @param clazz class
     * @return object list
     * @param <T> class type
     * @throws SpiNotFoundException
     */
    public static <T> List<T> getAll(Class<T> clazz) throws SpiNotFoundException {
        return applicationContext.getAll(clazz);
    }
}
