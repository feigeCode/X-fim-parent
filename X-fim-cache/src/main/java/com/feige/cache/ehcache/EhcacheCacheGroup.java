package com.feige.cache.ehcache;

import com.feige.api.cache.AbstractCacheGroup;
import com.feige.api.cache.LocalMapCache;
import com.feige.api.cache.LocalMapCacheOptions;
import com.feige.api.cache.MapCache;
import com.feige.api.cache.MapCacheOptions;
import com.feige.api.cache.ObjectCache;
import com.feige.api.cache.SetCache;

import java.io.Serializable;

/**
 * @author feige<br />
 * @ClassName: EhcacheCacheGroup <br/>
 * @Description: <br/>
 * @date: 2023/5/31 22:53<br/>
 */
public class EhcacheCacheGroup extends AbstractCacheGroup {
    public EhcacheCacheGroup(String groupName) {
        super(groupName);
    }

    @Override
    protected String getGroupedName(String name) {
        return null;
    }

    @Override
    public <K extends Serializable, V extends Serializable> MapCache<K, V> createMap(String name, Class<K> k, Class<V> v) {
        return null;
    }

    @Override
    public <K extends Serializable, V extends Serializable> MapCache<K, V> createMap(String name, MapCacheOptions<K, V> options, Class<K> k, Class<V> v) {
        return null;
    }

    @Override
    public <K extends Serializable, V extends Serializable> LocalMapCache<K, V> createLocalMap(String name, LocalMapCacheOptions<K, V> options, Class<K> k, Class<V> v) {
        return null;
    }

    @Override
    public <K extends Serializable, V extends Serializable> LocalMapCache<K, V> createLocalMap(String name, Class<K> k, Class<V> v) {
        return null;
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
