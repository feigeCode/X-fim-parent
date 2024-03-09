package com.feige.cache.local;


import com.feige.fim.cache.AbstractCacheManager;
import com.feige.fim.cache.Bucket;
import com.feige.fim.cache.CacheManager;
import com.feige.fim.cache.Cacheable;
import com.feige.fim.cache.MapCache;
import com.feige.utils.spi.annotation.SPI;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@SPI(value = "local", interfaces = CacheManager.class)
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
    public  Bucket<String> createBucket(String name) {
        LocalBucket<String> vLocalBucket = new LocalBucket<>(name);
        addCacheable(name, vLocalBucket);
        return vLocalBucket;
    }
}
