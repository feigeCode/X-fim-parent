package com.feige.api.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author feige<br />
 * @ClassName: AbstractCacheManager <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:19<br/>
 */
public abstract class AbstractCacheManager implements CacheManager {
    private final Map<String, CacheGroup> groups = new ConcurrentHashMap<String, CacheGroup>();


    @Override
    public CacheGroup getGroup(String name) {
        CacheGroup group = groups.get(name);
        if (group == null) {
            group = createGroup(name);
            if (group != null) {
                groups.putIfAbsent(name, group);
            }
        }
        return group;
    }

    protected abstract CacheGroup createGroup(String name);

}