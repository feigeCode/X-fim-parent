package com.feige.api.cache;

/**
 * @author feige<br />
 * @ClassName: MapCacheLoader <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:19<br/>
 */
public interface MapCacheLoader<K, V> {
    /**
     * Loads map value by key.
     *
     * @param key - map key
     * @return value or <code>null</code> if value doesn't exists
     */
    V load(K key);

    /**
     * Loads all keys.
     *
     * @return Iterable object. It's helpful if all keys don't fit in memory.
     */
    Iterable<K> loadAllKeys();
}