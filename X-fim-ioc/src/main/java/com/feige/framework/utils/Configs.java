package com.feige.framework.utils;


import com.feige.framework.annotation.SpiComp;
import com.feige.framework.api.config.Config;
import com.feige.framework.api.context.Environment;
import com.feige.fim.utils.StringUtils;
import com.feige.framework.api.context.EnvironmentAware;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@SpiComp
public class Configs implements EnvironmentAware  {

    public final static String CONFIG_FILE_KEY = "fim.path";
    public final static String DEFAULT_CONFIG_PATH = "conf" + File.separator + "fim.";
    

    public interface ConfigKey {
        /**
         * log config key
         */
        String LOG_DIR = "fim.log.dir";
        String LOG_LEVEL = "fim.log.level";
        String LOG_CONF_PATH = "fim.log.conf-path";


        /**
         * codec key
         */
        String CODEC_MAX_PACKET_SIZE_KEY = "fim.codec.max-packet-size";
        String CODEC_HEARTBEAT_KEY = "fim.codec.heartbeat";
        String CODEC_VERSION_KEY = "fim.codec.version";
        String CODEC_HEADER_LENGTH_KEY = "fim.codec.header-length";
        String CODEC_CHECK_SUM_KEY = "fim.codec.check-sum-key";
        
        /**
         * spi key
         */
        String SPI_LOADER_KEY = "spi.loader";
        String SPI_LOADER_TYPE = "spi.loader.type";
        
    }
    private static Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        Configs.environment = environment;
    }

    public static Environment getEnvironment() {
        return environment;
    }

    public static Config getCompositeConfig(){
        return environment.getCompositeConfig();
    }


    public static Config getSystemConfig(){
        return environment.getSystemConfig();
    }

    public static Config getAppConfig(){
        return environment.getAppConfig();
    }

    public static Config getEnvConfig(){
        return environment.getEnvConfig();
    }
    /**
     *  get int config
     * @param key key
     * @param defaultValue default value
     * @return int config
     */
    public static Integer getInt(String key, Integer defaultValue){
        return getCompositeConfig().getInt(key, defaultValue);
    }

    /**
     *  get int config
     * @param key key
     * @return int config
     */
    public static Integer getInt(String key){
        return getCompositeConfig().getInt(key);
    }


    /**
     *  get long config
     * @param key key
     * @param defaultValue default value
     * @return long config
     */
    public static Long getLong(String key, Long defaultValue){
        return getCompositeConfig().getLong(key, defaultValue);
    }

    /**
     *  get long config
     * @param key key
     * @return long config
     */
    public static Long getLong(String key){
        return getCompositeConfig().getLong(key);
    }

    /**
     *  get double config
     * @param key key
     * @param defaultValue default value
     * @return double config
     */
    public static Double getDouble(String key, Double defaultValue){
        return getCompositeConfig().getDouble(key);
    }

    /**
     *  get double config
     * @param key key
     * @return double config
     */
    public static Double getDouble(String key){
        return getCompositeConfig().getDouble(key);
    }

    /**
     * get string config
     * @param key key
     * @param defaultValue default value
     * @return string config
     */
    public static String getString(String key, String defaultValue){
        return getCompositeConfig().getString(key, defaultValue);
    }

    /**
     * get string config
     * @param key key
     * @return string config
     */
    public static String getString(String key){
        return getCompositeConfig().getString(key);
    }

    /**
     * get boolean config
     * @param key key
     * @param defaultValue default value
     * @return boolean config
     */
    public static Boolean getBoolean(String key, Boolean defaultValue){
        return getCompositeConfig().getBoolean(key, defaultValue);
    }

    /**
     * get boolean config
     * @param key key
     * @return boolean config
     */
    public static Boolean getBoolean(String key){
        return getCompositeConfig().getBoolean(key);
    }

    /**
     * get map config
     * @param key key
     * @return map config
     */
    public static Map<String, Object> getMap(String key){
        return getCompositeConfig().getMap(key);
    }

    /**
     * get list config
     * @param key key
     * @return list config
     */
    public static List<String> getList(String key){
        return getCompositeConfig().getList(key);
    }

    /**
     * get array config
     * @param key key
     * @return array config
     */
    public static String[] getArr(String key){
        return getCompositeConfig().getArr(key);
    }
    
    public static <T>  T get(Class<T> clazz, String key){
        return getCompositeConfig().convert(clazz, key, null);
    }

    /**
     * get stream by config key
     * @param key config key
     * @return stream
     */
    public static InputStream getInputStream(String key){
        String path = getString(key);
        if (StringUtils.isNotBlank(path)){
            try {
                return new FileInputStream(path);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

}