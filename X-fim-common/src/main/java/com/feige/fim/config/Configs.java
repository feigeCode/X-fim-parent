package com.feige.fim.config;


import com.feige.api.config.Config;
import com.feige.api.config.ConfigFactory;
import com.feige.fim.config.impl.CompositeConfig;
import com.feige.fim.config.impl.EnvConfig;
import com.feige.fim.config.impl.SystemConfig;
import com.feige.fim.lg.Loggers;
import com.feige.fim.spi.SpiLoaderUtils;

import java.io.File;
import java.util.List;
import java.util.Map;


public final class Configs {

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
         * server config key
         */
        String SERVER_ENABLE_EPOLL_KEY = "fim.server.enable-epoll";
        String SERVER_ENABLE_TCP_KEY = "fim.server.enable-tcp";
        String SERVER_TCP_IP_KEY = "fim.server.tcp-ip";
        String SERVER_TCP_PORT_KEY = "fim.server.tcp-port";
        String SERVER_ENABLE_WS_KEY = "fim.server.enable-ws";
        String SERVER_ENABLE_HTTP_KEY = "fim.server.enable-http";
        String SERVER_WS_IP_KEY = "fim.server.ws-ip";
        String SERVER_WS_PORT_KEY = "fim.server.ws-port";
        String SERVER_ENABLE_UDP_KEY = "fim.server.enable-udp";
        String SERVER_UDP_IP_KEY = "fim.server.udp-ip";
        String SERVER_UDP_PORT_KEY = "fim.server.udp-port";
        
        String SPI_LOADER_KEY = "fim.spi.loader";
    }

    private final static CompositeConfig COMPOSITE_CONFIG = new CompositeConfig();
    private final static Config SYSTEM_CONFIG = new SystemConfig();
    private final static Config ENV_CONFIG = new EnvConfig();
    private static Config APP_CONFIG = null;

    public static void loadConfig() throws Exception {
        COMPOSITE_CONFIG.addConfig(SYSTEM_CONFIG);
        COMPOSITE_CONFIG.addConfig(ENV_CONFIG);
        ConfigFactory configFactory = SpiLoaderUtils.getByConfig(ConfigFactory.class, true);
        APP_CONFIG = configFactory.create();
        COMPOSITE_CONFIG.addConfig(APP_CONFIG);
        Loggers.init();
    }

    public static Config getCompositeConfig(){
        return COMPOSITE_CONFIG;
    }


    public static Config getSystemConfig(){
        return SYSTEM_CONFIG;
    }

    public static Config getAppConfig(){
        return APP_CONFIG;
    }

    public static Config getEnvConfig(){
        return ENV_CONFIG;
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

}
