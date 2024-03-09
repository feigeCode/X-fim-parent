package com.feige.framework.utils;


import com.feige.framework.env.api.Config;
import com.feige.framework.env.api.Environment;
import com.feige.utils.common.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

public class Configs  {

    public final static String CONFIG_FILE_KEY = "fim.path";
    public final static String DEFAULT_CONFIG_PATH = "conf" + File.separator + "fim.";
    public static final String DEFAULT_MODULES_DIR = "modules" + File.separator;

    public interface ConfigKey {


        String MODULES_DIR_KEY = "fim.modules.dir";

        String SERVER_ENABLE_EPOLL_KEY = "fim.server.enable-epoll";
        /**
         * codec key
         */
        String CODEC_MAX_PACKET_SIZE_KEY = "fim.codec.max-packet-size";
        String CODEC_HEARTBEAT_KEY = "fim.codec.heartbeat";
        String CODEC_VERSION_KEY = "fim.codec.version";
        String CODEC_HEADER_LENGTH_KEY = "fim.codec.header-length";
        String CODEC_CHECK_SUM_KEY = "fim.codec.check-sum-key";

        String CRYPTO_ASYMMETRIC_PUBLIC_KEY = "fim.crypto.asymmetric.public-key";
        String CRYPTO_ENABLE = "fim.crypto.enable";
        String COMPRESS_ENABLE = "fim.compress.enable";
        String CRYPTO_SYMMETRIC_KEY_LENGTH = "fim.crypto.symmetric.key-length";

        String ENABLED_MODULE_NAMES = "fim.enabled.module.names";

        String ASSOCIATED_MODULE_NAME_KEY = "fim.associated.module.names";
    }
    private static Environment environment;
    
    
    public static File getFile(String path) {
        String absolutePath = new File("").getAbsolutePath();
        File file;
        
        if (Configs.DEFAULT_CONFIG_PATH.equals(path) || Configs.DEFAULT_MODULES_DIR.equals(path)){
            path = absolutePath + File.separator + path;
        }
        if (!path.endsWith("yaml") && !path.endsWith("yml")) {
            if (path.charAt(path.length() - 1) != '.') {
                path = path + ".";
            }
            File f = new File(path + "yaml");
            if (f.exists()) {
                path += "yaml";
            }else {
                path += "yml";
            }
        }
        file = new File(path);
        return file;
    }
    public static void setEnvironment(Environment environment) {
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
    public static Map<String, Object> getMapByKeyPrefix(String key){
        return getCompositeConfig().getMapByKeyPrefix(key);
    }

    /**
     * get list config
     * @param key key
     * @return list config
     */
    public static Collection<String> getCollection(String key){
        return getCompositeConfig().getCollection(key);
    }
    
    public static <T>  T get(Class<T> clazz, String key){
        return getCompositeConfig().convert(clazz, key, null);
    }

    public static void setConfig(String key, Object value){
        getAppConfig().setConfig(key, value);
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
