package com.feige.api.config;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public interface Config {

    /**
     * parse config file
     * @param file config file
     * @throws Exception
     */
    default void parseFile(File file) throws Exception {
        parseConfig(Files.newInputStream(file.toPath()));
    }
    /**
     * parse config object
     * @param config config object
     * @throws Exception
     */
    void parseConfig(Object config) throws Exception;
    
    

    /**
     *  get int config
     * @param key key
     * @param defaultValue default value
     * @return int config
     */
    Integer getInt(String key, Integer defaultValue);

    /**
     *  get int config
     * @param key key
     * @return int config
     */
    default Integer getInt(String key){
        return getInt(key, null);
    }


    /**
     *  get long config
     * @param key key
     * @param defaultValue default value
     * @return long config
     */
    Long getLong(String key, Long defaultValue);

    /**
     *  get long config
     * @param key key
     * @return long config
     */
    default Long getLong(String key){
        return getLong(key, null);
    }

    /**
     *  get double config
     * @param key key
     * @param defaultValue default value
     * @return double config
     */
    Double getDouble(String key, Double defaultValue);

    /**
     *  get double config
     * @param key key
     * @return double config
     */
    default Double getDouble(String key) {
        return getDouble(key, null);
    }

    /**
     * get string config
     * @param key key
     * @param defaultValue default value
     * @return string config
     */
    String getString(String key, String defaultValue);

    /**
     * get string config
     * @param key key
     * @return string config
     */
    default String getString(String key) {
        return getString(key, null);
    }

    /**
     * get boolean config
     * @param key key
     * @param defaultValue default value
     * @return boolean config
     */
    Boolean getBoolean(String key, Boolean defaultValue);

    /**
     * get boolean config
     * @param key key
     * @return boolean config
     */
    default Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    /**
     * get map config
     * @param key key
     * @return map config
     */
    Map<String, Object> getMap(String key);

    /**
     * get list config
     * @param key key
     * @return list config
     */
    List<String> getList(String key);

    /**
     * get array config
     * @param key key
     * @return array config
     */
    String[] getArr(String key);


    /**
     * get object
     * @param key key
     * @return object
     */
    Object getObject(String key);

    /**
     * get object 
     * @param key key
     * @param type type
     * @return object
     * @param <T> type
     */
    default <T> T getObject(String key, Class<T> type) {
        Object object = getObject(key);
        return type.cast(object);
    }

    /**
     * 序号
     * @return 序号
     */
    int order();
}
