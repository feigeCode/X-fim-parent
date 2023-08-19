package com.feige.cache;

import com.feige.api.cache.AbstractCacheManager;
import com.feige.api.cache.Bucket;
import com.feige.api.cache.Cacheable;
import com.feige.api.cache.MapCache;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class LocalCacheManager extends AbstractCacheManager {
    
    {
        Thread thread = new Thread(this::clearExpireCache, "clear-expire-cache");
        thread.setDaemon(true);
        thread.start();
    }

    public void clearExpireCache(){
        while (true){
            try {
                Iterator<Map.Entry<String, Cacheable>> iterator = this.instanceHolderPool.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String, Cacheable> next = iterator.next();
                    Cacheable value = next.getValue();
                    if (value instanceof Bucket && ((Bucket<?>) value).isExpired()){
                        System.out.println(value);
                        iterator.remove();
                    }
                }
                TimeUnit.MINUTES.sleep(1);
            }catch (Throwable ex){
                ex.printStackTrace();
            }
        }
        
    }

    @Override
    public <T extends Cacheable> T get(String name, Class<T> type) {
        T t = super.get(name, type);
        if (t instanceof Bucket){
            if (((Bucket<?>) t).isExpired()){
                return null;
            }
        }
        return t;
    }

    @Override
    public Cacheable get(String name) {
        Cacheable cacheable = super.get(name);
        if (cacheable instanceof Bucket){
            if (((Bucket<?>) cacheable).isExpired()) {
                return null;
            }
        }
        return cacheable;
    }

    @Override
    public <K extends Serializable, V extends Serializable> MapCache<K, V> createMapCache(String name, Class<K> k, Class<V> v) {
        LocalMapCache<K, V> mapCache = new LocalMapCache<>(name, new ConcurrentHashMap<>());
        addCacheable(name, mapCache);
        return mapCache;
    }

    @Override
    public <V extends Serializable> Bucket<V> createBucket(String name, Class<V> v) {
        LocalBucket<V> vLocalBucket = new LocalBucket<>(name);
        addCacheable(name, vLocalBucket);
        return vLocalBucket;
    }
}
