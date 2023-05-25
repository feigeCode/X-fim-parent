package com.feige.api.cache;

import java.io.Serializable;
import java.util.Set;

/**
 * @author feige<br />
 * @ClassName: CacheGroup <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:16<br/>
 */
public interface CacheGroup {
    Set<String> getNames();

    Cacheable get(String name);

    <T extends Cacheable> T get(String name, Class<T> type);

    <K extends Serializable, V extends Serializable> MapCache<K, V> createMap(String name, Class<K> k, Class<V> v);

    <K extends Serializable, V extends Serializable> MapCache<K, V> createMap(String name, MapCacheOptions<K, V> options, Class<K> k, Class<V> v);

    <K extends Serializable, V extends Serializable> LocalMapCache<K, V> createLocalMap(String name, LocalMapCacheOptions<K, V> options, Class<K> k, Class<V> v);

    <K extends Serializable, V extends Serializable> LocalMapCache<K, V> createLocalMap(String name, Class<K> k, Class<V> v);

    <V extends Serializable> SetCache<V> createSet(String name);
    
    <V extends Serializable> ObjectCache<V> createObject(String name);
    
    
}
