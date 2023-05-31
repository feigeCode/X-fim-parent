package com.feige.cache.ehcache;

import com.feige.api.cache.LocalMapCache;
import org.ehcache.Cache;

import java.io.Serializable;

/**
 * @author feige<br />
 * @ClassName: EhcacheLocalMapCache <br/>
 * @Description: <br/>
 * @date: 2023/5/31 22:58<br/>
 */
public class EhcacheLocalMapCache<K extends Serializable, V extends Serializable> extends EhcacheCacheMap<K, V> implements LocalMapCache<K, V> {
    public EhcacheLocalMapCache(String group, String name, Cache<K, V> cache) {
        super(group, name, cache);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return null;
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        return null;
    }
}
