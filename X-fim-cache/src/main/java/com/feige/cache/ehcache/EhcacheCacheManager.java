package com.feige.cache.ehcache;

import com.feige.api.cache.AbstractCacheManager;
import com.feige.api.cache.CacheGroup;

/**
 * @author feige<br />
 * @ClassName: EhcacheCacheManager <br/>
 * @Description: <br/>
 * @date: 2023/5/31 22:54<br/>
 */
public class EhcacheCacheManager extends AbstractCacheManager {
    @Override
    protected CacheGroup createGroup(String name) {
        return null;
    }

    @Override
    public CacheGroup getGroup(Class<?> clz) {
        return null;
    }
}
