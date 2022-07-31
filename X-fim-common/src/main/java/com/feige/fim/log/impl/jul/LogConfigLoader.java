package com.feige.fim.log.impl.jul;


import com.feige.fim.log.utils.ConfigUtil;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <p>The loader that responsible for loading Sentinel log configurations.</p>
 *
 * @author lianglin
 * @since 1.7.0
 */
public class LogConfigLoader {


    private static final String DEFAULT_LOG_CONFIG_FILE = "classpath:xiaofei-java-log.properties";

    private static final Properties properties = new Properties();

    static {
        try {
            load();
        } catch (Throwable t) {
            // NOTE: do not use RecordLog here, or there will be circular class dependency!
            System.err.println("[LogConfigLoader] Failed to initialize configuration items");
            t.printStackTrace();
        }
    }

    private static void load() {
        Properties p = ConfigUtil.loadProperties(DEFAULT_LOG_CONFIG_FILE);
        if (p != null && !p.isEmpty()) {
            properties.putAll(p);
        }

        CopyOnWriteArraySet<Map.Entry<Object, Object>> copy = new CopyOnWriteArraySet<>(System.getProperties().entrySet());
        for (Map.Entry<Object, Object> entry : copy) {
            String configKey = entry.getKey().toString();
            String newConfigValue = entry.getValue().toString();
            properties.put(configKey, newConfigValue);
        }
    }

    public static Properties getProperties() {
        return properties;
    }
}
