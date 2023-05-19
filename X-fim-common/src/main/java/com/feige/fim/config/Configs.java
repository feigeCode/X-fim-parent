package com.feige.fim.config;


import com.feige.api.config.Config;
import com.feige.api.config.ConfigFactory;
import com.feige.fim.spi.SpiLoader;
import com.feige.fim.utils.StringUtil;

import java.io.File;
import java.util.List;
import java.util.Map;


public final class Configs {

    public final static String CONFIG_FILE_KEY = "fim.path";
    public final static String DEFAULT_CONFIG_PATH = "conf" + File.separator + "fim.";

    public interface ConfigKey {
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
    }

    private static Config CONFIG;

    public static void loadConfig() throws Exception {
        ConfigFactory configFactory = SpiLoader.getInstance().getSpiByConfigOrPrimary(ConfigFactory.class);
        CONFIG = configFactory.create();
    }

    public static Config getConfig(){
        return CONFIG;
    }


    /**
     *  get int config
     * @param key key
     * @param defaultValue default value
     * @return int config
     */
    public static Integer getInt(String key, Integer defaultValue){
        return getConfig().getInt(key, defaultValue);
    }

    /**
     *  get int config
     * @param key key
     * @return int config
     */
    public static Integer getInt(String key){
        return getConfig().getInt(key);
    }


    /**
     *  get long config
     * @param key key
     * @param defaultValue default value
     * @return long config
     */
    public static Long getLong(String key, Long defaultValue){
        return getConfig().getLong(key, defaultValue);
    }

    /**
     *  get long config
     * @param key key
     * @return long config
     */
    public static Long getLong(String key){
        return getConfig().getLong(key);
    }

    /**
     *  get double config
     * @param key key
     * @param defaultValue default value
     * @return double config
     */
    public static Double getDouble(String key, Double defaultValue){
        return getConfig().getDouble(key);
    }

    /**
     *  get double config
     * @param key key
     * @return double config
     */
    public static Double getDouble(String key){
        return getConfig().getDouble(key);
    }

    /**
     * get string config
     * @param key key
     * @param defaultValue default value
     * @return string config
     */
    public static String getString(String key, String defaultValue){
        return getConfig().getString(key, defaultValue);
    }

    /**
     * get string config
     * @param key key
     * @return string config
     */
    public static String getString(String key){
        String value = System.getProperty(key);
        if (StringUtil.isNotBlank(value)){
            return value;
        }
        return getConfig().getString(key);
    }

    /**
     * get boolean config
     * @param key key
     * @param defaultValue default value
     * @return boolean config
     */
    public static Boolean getBoolean(String key, Boolean defaultValue){
        return getConfig().getBoolean(key, defaultValue);
    }

    /**
     * get boolean config
     * @param key key
     * @return boolean config
     */
    public static Boolean getBoolean(String key){
        return getConfig().getBoolean(key);
    }

    /**
     * get map config
     * @param key key
     * @return map config
     */
    public static Map<String, Object> getMap(String key){
        return getConfig().getMap(key);
    }

    /**
     * get list config
     * @param key key
     * @return list config
     */
    public static List<String> getList(String key){
        return getConfig().getList(key);
    }

    /**
     * get array config
     * @param key key
     * @return array config
     */
    public static String[] getArr(String key){
        return getConfig().getArr(key);
    }

}
