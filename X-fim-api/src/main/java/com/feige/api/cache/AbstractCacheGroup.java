package com.feige.api.cache;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author feige<br />
 * @ClassName: AbstractCacheGroup <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:19<br/>
 */
public abstract class AbstractCacheGroup implements CacheGroup {
    private final Map<String, Cacheable> instanceHolderPool = new ConcurrentHashMap<>();
    protected final String groupName;

    public AbstractCacheGroup(String groupName) {
        this.groupName = groupName;
    }

    final List<String> cacheNames = new CopyOnWriteArrayList<>();

    @Override
    public Set<String> getNames() {
        return new HashSet<>(cacheNames);
    }

    @Override
    public Cacheable get(String name) {
        return instanceHolderPool.get(getGroupedName(name));
    }

    @Override
    public <T extends Cacheable> T get(String name, Class<T> type) {
        Cacheable cache = instanceHolderPool.get(getGroupedName(name));
        if (cache != null) {
            return (T) cache;
        }
        return null;
    }

    protected abstract String getGroupedName(String name);

    protected void addCacheable(String key, String name, Cacheable cacheable) {
        instanceHolderPool.put(key, cacheable);
        cacheNames.add(name);
    }
}