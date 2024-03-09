package com.feige.cache.redis;


import com.feige.fim.cache.AbstractCacheable;
import com.feige.fim.cache.MapCache;
import org.redisson.api.RMap;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class RedisMapCache<K extends Serializable, V extends Serializable> extends AbstractCacheable implements MapCache<K,V> {
    private final RMap<K, V> rMap;
    
    public RedisMapCache(String name, RMap<K, V> rMap) {
        super(name);
        this.rMap = rMap;
    }

    @Override
    public int size() {
        return rMap.size();
    }

    @Override
    public boolean isEmpty() {
        return rMap.isEmpty();
    }

    @Override
    public void clear() {
        rMap.clear();
    }

    @Override
    public V put(K key, V value) {
        return rMap.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        rMap.putAll(map);
    }

    @Override
    public V get(K key) {
        return rMap.get(key);
    }

    @Override
    public boolean containsKey(K key) {
        return rMap.containsKey(key);
    }

    @Override
    public Map<K, V> getAll(Set<K> keys) {
        return rMap.getAll(keys);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        rMap.forEach(action);
    }

    @Override
    public V remove(K key) {
        return rMap.remove(key);
    }
}
