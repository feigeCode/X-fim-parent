package com.feige.framework.utils;

import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.spi.api.NoSuchInstanceException;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


/**
 * @author feige<br />
 * @ClassName: AppContext <br/>
 * @Description: <br/>
 * @date: 2023/5/20 15:13<br/>
 */

public class AppContext {

    
    @Getter
    @Setter
    private static ApplicationContext applicationContext;
    
    
    /**
     * get object by key
     * @param key key
     * @param clazz class
     * @return Object
     * @param <T> class type
     * @throws NoSuchInstanceException
     */
    public static <T> T get(String key, Class<T> clazz) throws NoSuchInstanceException {
        return applicationContext.get(key, clazz);
    }


    /**
     * get first object
     * @param clazz class
     * @return object
     * @param <T> class type
     * @throws NoSuchInstanceException
     */
    public static <T> T get(Class<T> clazz) throws NoSuchInstanceException {
        return applicationContext.get(clazz);
    }


    /**
     * get all object
     * @param clazz class
     * @return object list
     * @param <T> class type
     * @throws NoSuchInstanceException
     */
    public static <T> Map<String, T> getByType(Class<T> clazz) throws NoSuchInstanceException {
        return applicationContext.getByType(clazz);
    }
}
