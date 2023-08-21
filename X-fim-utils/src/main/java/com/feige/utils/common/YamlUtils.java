package com.feige.utils.common;

import com.feige.utils.logger.Loggers;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.reader.UnicodeReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * copy form springboot YamlProcessor.java
 */
public class YamlUtils {

    private static final Logger LOG = Loggers.UTILS;
    /**
     * Single yaml file parser
     * @param is    config stream
     * @return
     * @throws IOException
     */
    public static Map<String, Object> parser(InputStream is) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        try (UnicodeReader reader = new UnicodeReader(is)) {
            Yaml yaml = new Yaml();
            for (Object object : yaml.loadAll(reader)){
                if (object != null) {
                    Map<String, Object> map = asMap(object);
                    buildFlattenedMap(result, map, null);
                }
            }
        } catch (Exception e) {
            LOG.error("single yaml file processing error:", e);
            throw new RuntimeException("single yaml file processing error:", e);
        }
        return result;
    }

    private static Map<String, Object> asMap(Object object) {
        // YAML can have numbers as keys
        Map<String, Object> result = new LinkedHashMap<>();
        if (!(object instanceof Map)) {
            // A document can be a text literal
            result.put("document", object);
            return result;
        }

        Map<Object, Object> map = (Map<Object, Object>) object;
        map.forEach((key, value) -> {
            if (value instanceof Map) {
                value = asMap(value);
            }
            if (key instanceof CharSequence) {
                result.put(key.toString(), value);
            } else {
                // It has to be a map key in this case
                result.put("[" + key.toString() + "]", value);
            }
        });
        return result;
    }

    private static void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, String path) {
        //循环读取原数据
        source.forEach((key, value) -> {
            //如果存在路径进行拼接
            if (StringUtils.isNotBlank(path)) {
                if (key.startsWith("[")) {
                    key = path + key;
                } else {
                    key = path + '.' + key;
                }
            }
            //数据类型匹配
            if (value instanceof String) {
                result.put(key, value);
            } else if (value instanceof Map) {
                //如果是map,就继续读取
                Map<String, Object> map = (Map<String, Object>)value;
                buildFlattenedMap(result, map, key);
            } else if (value instanceof Collection) {
                Collection<Object> collection = (Collection<Object>) value;
//                if (collection.isEmpty()) {
//                    result.put(key, "");
//                } else {
//                    int count = 0;
//
//                    for (Object object : collection) {
//                        buildFlattenedMap(result, Collections.singletonMap("[" + count++ + "]", object), key);
//                    }
//                }
                result.put(key, value);
            } else {
                result.put(key, value != null ? value : "");
            }
        });
    }

}
