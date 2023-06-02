package com.feige.cache.ehcache;

import com.feige.api.cache.AbstractCacheGroup;
import com.feige.api.cache.LocalMapCache;
import com.feige.api.cache.LocalMapCacheOptions;
import com.feige.api.cache.MapCache;
import com.feige.api.cache.MapCacheOptions;
import com.feige.api.cache.ObjectCache;
import com.feige.api.cache.SetCache;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.io.Serializable;

/**
 * @author feige<br />
 * @ClassName: EhcacheCacheGroup <br/>
 * @Description: <br/>
 * @date: 2023/5/31 22:53<br/>
 */
public class EhcacheCacheGroup extends AbstractCacheGroup {

    public static final String UNDERLINE = "_";
    private final EhcacheCacheManager manager;
    private final CacheManager cacheManager;

    public EhcacheCacheGroup(String groupName, EhcacheCacheManager manager) {
        super(groupName);
        this.manager = manager;
        this.cacheManager = manager.getCacheManager();
    }

    @Override
    protected String getGroupedName(String name) {
        return this.groupName + UNDERLINE + name;
    }

    @Override
    public <K extends Serializable, V extends Serializable> MapCache<K, V> createMap(String name, Class<K> k, Class<V> v) {
        return createMap(name, MapCacheOptions.defaults(), k, v);
    }
    
    protected  <K extends Serializable, V extends Serializable> Cache<K, V> buildCache(String name, MapCacheOptions<K, V> options, Class<K> k, Class<V> v){
        String key = this.getGroupedName(name);
        long maximumSize = options.getMaximumSize();
        if (maximumSize <= 0){
            maximumSize = manager.getDefaultCacheSize();
        }
        CacheConfigurationBuilder<K, V> builder = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(k, v, ResourcePoolsBuilder.heap(maximumSize));
        if (options.getTimeToLive() != null){
            builder.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(options.getTimeToLive()));
        }
        CacheConfiguration<K, V> cacheConfiguration = builder.build();
        return cacheManager.createCache(key, cacheConfiguration);
    }

    @Override
    public <K extends Serializable, V extends Serializable> MapCache<K, V> createMap(String name, MapCacheOptions<K, V> options, Class<K> k, Class<V> v) {
        Cache<K, V> cache = buildCache(name, options, k, v);
        EhcacheCacheMap<K, V> cacheMap = new EhcacheCacheMap<>(this.groupName, name, cache, options.getLoader());
        addCacheable(this.getGroupedName(name), name, cacheMap);
        return cacheMap;
    }

    @Override
    public <K extends Serializable, V extends Serializable> LocalMapCache<K, V> createLocalMap(String name, LocalMapCacheOptions<K, V> options, Class<K> k, Class<V> v) {
        Cache<K, V> cache = buildCache(name, options, k, v);
        EhcacheLocalMapCache<K, V> map = new EhcacheLocalMapCache<K, V>(this.groupName, name, cache, options.getLoader());
        addCacheable(this.getGroupedName(name), name, map);
        return map;
    }

    @Override
    public <K extends Serializable, V extends Serializable> LocalMapCache<K, V> createLocalMap(String name, Class<K> k, Class<V> v) {
        return createLocalMap(name, LocalMapCacheOptions.defaults(), k, v);
    }

    @Override
    public <V extends Serializable> SetCache<V> createSet(String name) {
        return null;
    }

    @Override
    public <V extends Serializable> ObjectCache<V> createObject(String name) {
        return null;
    }
}
