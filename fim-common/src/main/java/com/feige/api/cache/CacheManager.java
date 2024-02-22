package com.feige.api.cache;

import java.io.Serializable;
import java.util.Set;

/**
 * @author feige<br />
 * @ClassName: CacheManager <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:04<br/>
 */
public interface CacheManager {

    Set<String> getNames();

    Cacheable get(String name);

    <T extends Cacheable> T get(String name, Class<T> type);

    <K extends Serializable, V extends Serializable> MapCache<K, V> createMapCache(String name, Class<K> k, Class<V> v);
    
    
    Bucket<String> createBucket(String name);
}
