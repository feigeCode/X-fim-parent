package com.feige.framework.config;


import com.feige.api.config.Config;
import com.feige.api.config.ConfigFactory;
import com.feige.framework.config.impl.CompositeConfig;
import com.feige.framework.config.impl.EnvConfig;
import com.feige.framework.config.impl.SystemConfig;
import com.feige.fim.utils.StringUtils;
import com.google.common.base.Splitter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;


public final class Configs {

    public final static String CONFIG_FILE_KEY = "fim.path";
    public final static String DEFAULT_CONFIG_PATH = "conf" + File.separator + "fim.";
    public final static Splitter equalsSplitter = Splitter.on("=").omitEmptyStrings();
    public final static Splitter commaSplitter = Splitter.on(",").omitEmptyStrings();

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
        String SERVER_ENABLE_TCP_KEY = "fim.server.tcp.enable";
        String SERVER_ENABLE_TCP_SSL = "fim.server.tcp.ssl.enable";
        String SERVER_ENABLE_TCP_K_C_P = "fim.server.tcp.ssl.key-cert-chain-path";
        String SERVER_ENABLE_TCP_P_K_P = "fim.server.tcp.ssl.private-key-path";
        String SERVER_ENABLE_TCP_K_P = "fim.server.tcp.ssl.key-password";
        String SERVER_ENABLE_TCP_T_C_P = "fim.server.tcp.ssl.trust-cert-collection-path";
        String SERVER_TCP_IP_KEY = "fim.server.tcp.ip";
        String SERVER_TCP_PORT_KEY = "fim.server.tcp.port";
        String SERVER_ENABLE_WS_KEY = "fim.server.ws.enable";
        String SERVER_ENABLE_WS_SSL = "fim.server.ws.ssl.enable";
        String SERVER_ENABLE_WS_K_C_P = "fim.server.tcp.ssl.key-cert-chain-path";
        String SERVER_ENABLE_WS_P_K_P = "fim.server.tcp.ssl.private-key-path";
        String SERVER_ENABLE_WS_K_P = "fim.server.tcp.ssl.key-password";
        String SERVER_ENABLE_WS_T_C_P = "fim.server.tcp.ssl.trust-cert-collection-path";
        String SERVER_ENABLE_HTTP_KEY = "fim.server.http.enable";
        String SERVER_WS_IP_KEY = "fim.server.ws.ip";
        String SERVER_WS_PORT_KEY = "fim.server.ws.port";
        String SERVER_ENABLE_UDP_KEY = "fim.server.udp.enable";
        String SERVER_UDP_IP_KEY = "fim.server.udp.ip";
        String SERVER_UDP_PORT_KEY = "fim.server.udp.port";


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

        /**
         * 注册中心
         */
        String NACOS_SERVER_LIST_KEY = "nacos.server-list";
        
        String REDIS_TYPE = "redis.type";
        String REDIS_CONFIG = "redis.config";
        
    }

    private final static CompositeConfig COMPOSITE_CONFIG = new CompositeConfig();
    private final static Config SYSTEM_CONFIG = new SystemConfig();
    private final static Config ENV_CONFIG = new EnvConfig();
    private static Config APP_CONFIG = null;


    public static void loadConfig() throws Exception {
        COMPOSITE_CONFIG.addConfig(SYSTEM_CONFIG);
        COMPOSITE_CONFIG.addConfig(ENV_CONFIG);
        ServiceLoader<ConfigFactory> loader = ServiceLoader.load(ConfigFactory.class);
        Iterator<ConfigFactory> iterator = loader.iterator();
        if (iterator.hasNext()){
            APP_CONFIG = iterator.next().create();
        }else {
            throw new RuntimeException(ConfigFactory.class.getName() + "未发现任何实现，请检查META-INF/services目录下的配置是否正常!");
        }
        COMPOSITE_CONFIG.addConfig(APP_CONFIG);
        initLogConfig();
    }
    


    public static void initLogConfig() {
        System.setProperty("log.home", Configs.getString(Configs.ConfigKey.LOG_DIR));
        System.setProperty("log.root.level", Configs.getString(Configs.ConfigKey.LOG_LEVEL));
        System.setProperty("logback.configurationFile", Configs.getString(Configs.ConfigKey.LOG_CONF_PATH));
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
