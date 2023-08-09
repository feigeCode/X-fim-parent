package com.feige.framework.api.config;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface Config {
    
    default void setConfig(String key, Object value){
        throw new UnsupportedOperationException();
    }
    
    /**
     *  get int config
     * @param key key
     * @param defaultValue default value
     * @return int config
     */
    default Integer getInt(String key, Integer defaultValue) {
        return convert(Integer.class, key, defaultValue);
    }

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
    default Long getLong(String key, Long defaultValue) {
        return convert(Long.class, key, defaultValue);
    }


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
    default Double getDouble(String key, Double defaultValue) {
        return convert(Double.class, key, defaultValue);
    }

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
    default String getString(String key, String defaultValue) {
        return convert(String.class, key, defaultValue);
    }

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
    default Boolean getBoolean(String key, Boolean defaultValue) {
        return convert(Boolean.class, key, defaultValue);
    }

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
    default Map<String, Object> getMap(String key) {
        return convert(Map.class, key, null);
    }

    /**
     * get list config
     * @param key key
     * @return list config
     */
    default List<String> getList(String key) {
        return convert(List.class, key, null);
    }

    /**
     * get array config
     * @param key key
     * @return array config
     */
    default String[] getArr(String key) {
        return convert(String[].class, key, null);
    }


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
        return convert(type, key, null);
    }

    /**
     * order
     * @return order
     */
    int order();
    
    default <T> T convert(Class<T> cls, String key, T defaultValue){
        Object answer = this.getObject(key);
        if (answer == null){
            return defaultValue;
        }
        if (cls.isInstance(answer)){
            return cls.cast(answer);
        }
        if (Boolean.class.equals(cls) || Boolean.TYPE.equals(cls)){
            if (answer instanceof String) {
                answer = Boolean.valueOf((String)answer);
            }
            if (answer instanceof Number) {
                Number n = (Number)answer;
                answer = n.intValue() != 0 ? Boolean.TRUE : Boolean.FALSE;
            }
        }else if (Number.class.isAssignableFrom(cls) || cls.isPrimitive()){
            if (answer instanceof String) {
                try {
                    String text = (String)answer;
                    answer = NumberFormat.getInstance().parse(text);
                } catch (ParseException ignored) {}
            }
            if (answer instanceof Number) {
                if (Integer.class.equals(cls) || Integer.TYPE.equals(cls)) {
                    answer = ((Number) answer).intValue();
                } else if (Long.class.equals(cls) || Long.TYPE.equals(cls)) {
                    answer = ((Number) answer).longValue();
                } else if (Byte.class.equals(cls) || Byte.TYPE.equals(cls)) {
                    answer = ((Number) answer).byteValue();
                } else if (Short.class.equals(cls) || Short.TYPE.equals(cls)) {
                    answer = ((Number) answer).shortValue();
                } else if (Float.class.equals(cls) || Float.TYPE.equals(cls)) {
                    answer = ((Number) answer).floatValue();
                } else if (Double.class.equals(cls) || Double.TYPE.equals(cls)) {
                    answer = ((Number) answer).doubleValue();
                }
            }
        }else if (cls.isEnum()) {
            answer = Enum.valueOf(cls.asSubclass(Enum.class), (String) answer);
        }
        if (cls.isPrimitive()){
            return (T) answer;
        }
        return cls.cast(answer); 
    }
}
