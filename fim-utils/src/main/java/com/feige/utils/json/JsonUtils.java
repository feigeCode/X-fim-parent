package com.feige.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.feige.utils.common.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author feige
 */
@Slf4j
public class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectMapper ORIGIN_MAPPER;
    private static final ObjectMapper NOT_INCLUDE_NULL_MAPPER;
    private static final ObjectMapper PROCESS_NUMBER_MAPPER;
    private static final ObjectMapper ENUM_TO_CODE_MAPPER;


    public static void writeJson(Writer writer, Object obj) {
        if (obj == null){
            return;
        }
        try {
            MAPPER.writeValue(writer, obj);
        } catch (IOException e) {
            log.warn("write to json string error:", e);
        }
    }


    public static void writeJson(OutputStream os, Object obj) {
        if (obj == null){
            return;
        }
        try {
            MAPPER.writeValue(os, obj);
        } catch (IOException e) {
            log.warn("write to json string error:", e);
        }
    }
    public static byte[] toJsonBytes(Object object) {
        if (object == null) {
            return null;
        } else {
            try {
                return MAPPER.writeValueAsBytes(object);
            } catch (Exception e) {
                log.warn("write to json string error:" + object, e);
                return null;
            }
        }
    }
    
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        } else {
            try {
                return MAPPER.writeValueAsString(object);
            } catch (Exception e) {
                log.warn("write to json string error:" + object, e);
                return null;
            }
        }
    }

    public static String toJsonEnumToCode(Object object) {
        if (object == null) {
            return null;
        } else {
            try {
                return ENUM_TO_CODE_MAPPER.writeValueAsString(object);
            } catch (Exception e) {
                log.warn("write to json string error:" + object, e);
                return null;
            }
        }
    }

    public static String toJson(Object object, boolean longToString) {
        return longToString ? toJsonLongToString(object) : toJson(object);
    }

    public static String toJsonLongToString(Object object) {
        if (object == null) {
            return null;
        } else {
            try {
                return PROCESS_NUMBER_MAPPER.writeValueAsString(object);
            } catch (Exception e) {
                log.warn("write to json string error:" + object, e);
                return null;
            }
        }
    }

    public static String toJsonDecimalToString(Object object) {
        if (object == null) {
            return null;
        } else {
            try {
                return PROCESS_NUMBER_MAPPER.writeValueAsString(object);
            } catch (Exception e) {
                log.warn("write to json string error:" + object, e);
                return null;
            }
        }
    }

    public static String toJsonNotIncludeNull(Object object) {
        if (object == null) {
            return null;
        } else {
            try {
                return NOT_INCLUDE_NULL_MAPPER.writeValueAsString(object);
            } catch (Exception e) {
                log.warn("write to json string error:" + object, e);
                return null;
            }
        }
    }

    public static <T> T fromJson(byte[] bytes, Class<T> clazz) {
        if (bytes.length <= 0) {
            return null;
        } else {
            try {
                return readValue(bytes, clazz);
            } catch (IOException e) {
                log.warn("parse json string error:" + new String(bytes), e);
                return null;
            }
        }
    }
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            try {
                return readValue(jsonString, clazz);
            } catch (IOException e) {
                log.warn("parse json string error:" + jsonString, e);
                return null;
            }
        }
    }
    

    public static <T, V> T fromJson(String jsonString, Class<T> clazz, Class<V> generalClazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            try {
                JavaType javaType = MAPPER.getTypeFactory().constructParametricType(clazz, generalClazz);
                return readValue(jsonString, javaType);
            } catch (IOException e) {
                log.warn("parse json string error:" + jsonString, e);
                return null;
            }
        }
    }

    public static Map<String, String> toStringMap(String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            return Collections.emptyMap();
        } else {
            HashMap<String, String> jsonMap = new HashMap<>();
            try {
                JsonNode jsonNode = MAPPER.readTree(jsonString);
                jsonNode.fieldNames().forEachRemaining((name) -> jsonMap.put(name, jsonNode.get(name).isTextual() ? jsonNode.get(name).textValue() : jsonNode.get(name).toString()));
                return jsonMap;
            } catch (Exception e) {
                log.warn("parse json string error:" + jsonString, e);
                return null;
            }
        }
    }

    public static <K, V> Map<K, V> toMap(String jsonString, Class<K> keyClass, Class<V> valueClass) {
        if (StringUtils.isEmpty(jsonString)) {
            return Collections.emptyMap();
        } else {
            try {
                JavaType javaType = MAPPER.getTypeFactory().constructParametricType(LinkedHashMap.class, keyClass, valueClass);
                return readValue(jsonString, javaType);
            } catch (Exception e) {
                log.warn("parse json string error:" + jsonString, e);
                return null;
            }
        }
    }

    public static Map<String, Object> toMap(String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            return Collections.emptyMap();
        } else {
            try {
                return readValue(jsonString, Map.class);
            } catch (Exception e) {
                log.warn("parse json string error:" + jsonString, e);
                return null;
            }
        }
    }

    public static List<String> toStringList(String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            return Collections.emptyList();
        } else {
            ArrayList<String> jsonList = new ArrayList<>();

            try {
                ArrayNode arrayNode = (ArrayNode) MAPPER.readTree(jsonString);

                for (JsonNode node : arrayNode) {
                    jsonList.add(node.toString());
                }
                return jsonList;
            } catch (Exception e) {
                log.warn("{}, parse json string error: {}", e.getMessage(), jsonString);
                return null;
            }
        }
    }

    public static List<?> toList(String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            return Collections.emptyList();
        } else {
            try {
                return readValue(jsonString, List.class);
            } catch (Exception e) {
                log.warn("{}, parse json string error: {}", e.getMessage(), jsonString);
                return null;
            }
        }
    }

    public static <T> List<T> toList(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return Collections.emptyList();
        } else {
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, clazz);

            try {
                return readValue(jsonString, javaType);
            } catch (Exception e) {
                log.warn("parse json string error:" + jsonString, e);
                return null;
            }
        }
    }

    private static <T> T readValue(String content, JavaType valueType) throws JsonProcessingException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        TypeBindings typeBindings = valueType.getBindings();
        Thread.currentThread().setContextClassLoader(typeBindings.getBoundType(typeBindings.size() - 1).getRawClass().getClassLoader());
        try {
            return MAPPER.readValue(content, valueType);
        } catch (Throwable var9) {
            return ORIGIN_MAPPER.readValue(content, valueType);
        } finally {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
    }

    private static <T> T readValue(String content, Class<T> valueType) throws JsonProcessingException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(valueType.getClassLoader());
        try {
            return MAPPER.readValue(content, valueType);
        } catch (Throwable var8) {
            return ORIGIN_MAPPER.readValue(content, valueType);
        } finally {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
    }
    private static <T> T readValue(byte[] content, Class<T> valueType) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(valueType.getClassLoader());
        try {
            return MAPPER.readValue(content, valueType);
        } catch (Throwable var8) {
            return ORIGIN_MAPPER.readValue(content, valueType);
        } finally {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
    }
    

    static {

        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        MAPPER.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        MAPPER.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        MAPPER.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
        MAPPER.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);
        ENUM_TO_CODE_MAPPER = new ObjectMapper();
        ENUM_TO_CODE_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        ENUM_TO_CODE_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        ENUM_TO_CODE_MAPPER.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        ENUM_TO_CODE_MAPPER.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        ENUM_TO_CODE_MAPPER.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
        ENUM_TO_CODE_MAPPER.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);
        SimpleModule simpleModule1 = new SimpleModule();
        ENUM_TO_CODE_MAPPER.registerModule(simpleModule1);
        ORIGIN_MAPPER = new ObjectMapper();
        ORIGIN_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        ORIGIN_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        ORIGIN_MAPPER.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        ORIGIN_MAPPER.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        ORIGIN_MAPPER.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
        ORIGIN_MAPPER.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);
        PROCESS_NUMBER_MAPPER = new ObjectMapper();
        PROCESS_NUMBER_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        PROCESS_NUMBER_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        PROCESS_NUMBER_MAPPER.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        PROCESS_NUMBER_MAPPER.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        PROCESS_NUMBER_MAPPER.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
        PROCESS_NUMBER_MAPPER.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        module.addSerializer(Double.class, ToStringSerializer.instance);
        module.addSerializer(Double.TYPE, ToStringSerializer.instance);
        module.addSerializer(Float.class, ToStringSerializer.instance);
        module.addSerializer(Float.TYPE, ToStringSerializer.instance);
        module.addSerializer(Short.class, ToStringSerializer.instance);
        module.addSerializer(Short.TYPE, ToStringSerializer.instance);
        module.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        PROCESS_NUMBER_MAPPER.registerModule(module);
        NOT_INCLUDE_NULL_MAPPER = new ObjectMapper();
        NOT_INCLUDE_NULL_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        NOT_INCLUDE_NULL_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        NOT_INCLUDE_NULL_MAPPER.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);
        NOT_INCLUDE_NULL_MAPPER.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        NOT_INCLUDE_NULL_MAPPER.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        NOT_INCLUDE_NULL_MAPPER.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
        NOT_INCLUDE_NULL_MAPPER.setSerializationInclusion(Include.NON_NULL);
    }
}
