package com.feige.cache.ehcache;

import com.feige.api.cache.AbstractCacheable;
import com.feige.api.cache.MapCache;
import com.feige.api.cache.MapCacheLoader;
import org.ehcache.Cache;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;

/**
 * @author feige<br />
 * @ClassName: EhcacheCacheMap <br/>
 * @Description: <br/>
 * @date: 2023/5/31 22:56<br/>
 */
public class EhcacheCacheMap<K extends Serializable, V extends Serializable> extends AbstractCacheable implements MapCache<K, V> {
    
    protected Cache<K, V> cache;
    private ConcurrentMap<K, ReentrantLock> loadLocks;
    private final MapCacheLoader<K, V> loader;

    public EhcacheCacheMap(String group, String name, Cache<K,V> cache, MapCacheLoader<K, V> loader) {
        super(group, name);
        this.cache = cache;
        this.loader = loader;
        if (!hasNoLoader()) {
            this.loadLocks = new ConcurrentHashMap<>();
        }
    }

    protected boolean hasNoLoader() {
        return this.loader == null;
    }

    private boolean doTryLock(ReentrantLock aLock) {
        try {
            //5秒不释放，忽略
            return aLock.tryLock(5, TimeUnit.SECONDS);
        } catch (Exception ex) {
            return false;
        }
    }

    private void doUnlock(K key, ReentrantLock locker) {
        if (locker.isLocked()) {
            locker.unlock();
        }
        loadLocks.remove(key);
    }

    private ReentrantLock getKeyLoadLock(K aKey) {
        ReentrantLock result, fTempLock;
        // 按对应的Key值进行锁定加载,避免加载多次
        result = loadLocks.get(aKey);
        if (result != null) {
            return result;
        }
        result = new ReentrantLock();
        fTempLock = loadLocks.putIfAbsent(aKey, result);
        if (fTempLock != null) {
            return fTempLock;
        }

        return result;
    }

    private V loadValue(K key) {
        if (hasNoLoader()){
            return null;
        }
        // 按对应的Key值进行锁定加载,避免加载多次
        ReentrantLock locker = getKeyLoadLock(key);
        boolean locked = doTryLock(locker);
        try {
            if (locked) {
                return loader.load(key);
            }
        } finally {
            doUnlock(key, locker);
        }
        return null;
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
        if (loader == null) {
            return;
        }
        Iterable<K> iterable = loader.loadAllKeys();
        iterable.forEach(key -> {
            V value = loader.load(key);
            if (replaceExistingValues) {
                put(key, value);
            } else {
                putIfAbsent(key, value);
            }
        });
    }

    public V putIfAbsent(K key, V value) {
        return cache.putIfAbsent(key, value);
    }

    @Override
    public V get(K key) {
        if (!hasNoLoader()) {
            V value = cache.get(key);
            if (value == null) {
                value = loadValue(key);
                if (value != null) {
                    this.put(key, value);
                }
            }
        }
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
