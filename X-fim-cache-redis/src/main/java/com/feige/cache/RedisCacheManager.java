package com.feige.cache;

import com.feige.api.cache.AbstractCacheManager;
import com.feige.api.cache.MapCache;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.io.Serializable;

public class RedisCacheManager extends AbstractCacheManager {
    private final RedissonClient client;

    public RedisCacheManager(RedissonClient client) {
        this.client = client;
    }

    @Override
    public <K extends Serializable, V extends Serializable> MapCache<K, V> createMapCache(String name, Class<K> k, Class<V> v) {
        RMap<K, V> rMap = client.getMap(name);
        RedisMapCache<K, V> cache = new RedisMapCache<>(name, rMap);
        addCacheable(name, cache);
        return cache;
    }
}
