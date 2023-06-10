package com.feige.api.cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author feige<br />
 * @ClassName: AbstractCacheManager <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:19<br/>
 */
public abstract class AbstractCacheManager implements CacheManager {
    
    private final Map<String, Cacheable> instanceHolderPool = new ConcurrentHashMap<>();

    @Override
    public Set<String> getNames() {
        return new HashSet<>(instanceHolderPool.keySet());
    }

    @Override
    public Cacheable get(String name) {
        return instanceHolderPool.get(name);
    }

    @Override
    public <T extends Cacheable> T get(String name, Class<T> type) {
        Cacheable cache = instanceHolderPool.get(name);
        if (cache != null) {
            return (T) cache;
        }
        return null;
    }
    
    protected void addCacheable(String name, Cacheable cacheable) {
        instanceHolderPool.put(name, cacheable);
    }

}
