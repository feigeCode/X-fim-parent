package com.feige.cache.ehcache;

import com.feige.api.cache.LocalMapCache;
import com.feige.api.cache.MapCacheLoader;
import org.ehcache.Cache;

import java.io.Serializable;

/**
 * @author feige<br />
 * @ClassName: EhcacheLocalMapCache <br/>
 * @Description: <br/>
 * @date: 2023/5/31 22:58<br/>
 */
public class EhcacheLocalMapCache<K extends Serializable, V extends Serializable> extends EhcacheCacheMap<K, V> implements LocalMapCache<K, V> {

    
    public EhcacheLocalMapCache(String group, String name, Cache<K, V> cache, MapCacheLoader<K, V> loader) {
        super(group, name, cache, loader);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return cache.putIfAbsent(key, value);
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        V v = super.get(key);
        return v == null ? defaultValue : v;
    }

}
