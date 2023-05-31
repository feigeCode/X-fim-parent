package com.feige.cache.ehcache;

import com.feige.api.cache.AbstractCacheable;
import com.feige.api.cache.MapCache;
import org.ehcache.Cache;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * @author feige<br />
 * @ClassName: EhcacheCacheMap <br/>
 * @Description: <br/>
 * @date: 2023/5/31 22:56<br/>
 */
public class EhcacheCacheMap<K extends Serializable, V extends Serializable> extends AbstractCacheable implements MapCache<K, V> {
    
    protected Cache<K, V> cache;

    public EhcacheCacheMap(String group, String name, Cache<K,V> cache) {
        super(group, name);
        this.cache = cache;
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        Iterator<Cache.Entry<K, V>> it = cache.iterator();
        boolean next = it.hasNext();
        return !next;
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public V put(K key, V value) {
        cache.put(key, value);
        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        cache.putAll(map);
    }

    @Override
    public void clearAndPutAll(Map<? extends K, ? extends V> map) {
        cache.clear();
        cache.putAll(map);
    }

    @Override
    public void loadAll(boolean replaceExistingValues) {

    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    @Override
    public Map<K, V> getAll(Set<K> keys) {
        return cache.getAll(keys);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        cache.forEach(kvEntry -> action.accept(kvEntry.getKey(), kvEntry.getValue()));
    }

    @Override
    public V remove(K key) {
        V v = this.get(key);
        cache.remove(key);
        return v;
    }
}
