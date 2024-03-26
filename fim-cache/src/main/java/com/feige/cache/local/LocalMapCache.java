package com.feige.cache.local;


import com.feige.fim.cache.AbstractCacheable;
import com.feige.fim.cache.MapCache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class LocalMapCache<K extends Serializable, V extends Serializable>  extends AbstractCacheable implements MapCache<K, V> {
    
    private final Map<K, V> innerCache;
    
    public LocalMapCache(String name, Map<K, V> innerCache) {
        super(name);
        this.innerCache = innerCache;
    }

    @Override
    public int size() {
        return innerCache.size();
    }

    @Override
    public boolean isEmpty() {
        return innerCache.isEmpty();
    }

    @Override
    public void clear() {
        innerCache.clear();
    }

    @Override
    public V put(K key, V value) {
        return innerCache.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        innerCache.putAll(map);
    }

    @Override
    public V get(K key) {
        return innerCache.get(key);
    }

    @Override
    public boolean containsKey(K key) {
        return innerCache.containsKey(key);
    }

    @Override
    public Map<K, V> getAll(Set<K> keys) {
        Map<K, V> map = new HashMap<>();
        if (keys != null && !keys.isEmpty()){
            for (K key : keys) {
                V v = innerCache.get(key);
                if (v != null){
                    map.put(key, v);
                }
            }
        }
        return map;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        innerCache.forEach(action);
    }

    @Override
    public V remove(K key) {
        return innerCache.remove(key);
    }
}
