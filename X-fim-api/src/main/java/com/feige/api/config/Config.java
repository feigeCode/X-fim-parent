package com.feige.api.config;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface Config {

    /**
     * parse config file
     * @param file config file
     * @throws Exception
     */
    void parseFile(File file) throws Exception;
    /**
     * parse config file
     * @param is config file stream
     * @throws Exception
     */
    void parseFile(InputStream is) throws Exception;

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
    Integer getInt(String key);


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
    Long getLong(String key);

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
    Double getDouble(String key);

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
    String getString(String key);

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
    Boolean getBoolean(String key);

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
}
