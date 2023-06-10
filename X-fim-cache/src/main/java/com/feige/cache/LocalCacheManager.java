package com.feige.cache;

import com.feige.api.cache.AbstractCacheManager;
import com.feige.api.cache.MapCache;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class LocalCacheManager extends AbstractCacheManager {
    
    @Override
    public <K extends Serializable, V extends Serializable> MapCache<K, V> createMapCache(String name, Class<K> k, Class<V> v) {
        LocalMapCache<K, V> mapCache = new LocalMapCache<>(name, new ConcurrentHashMap<>());
        addCacheable(name, mapCache);
        return mapCache;
    }
}
