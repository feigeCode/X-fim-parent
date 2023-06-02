package com.feige.cache.ehcache;

import com.feige.api.cache.AbstractCacheManager;
import com.feige.api.cache.CacheGroup;
import org.ehcache.CacheManager;

/**
 * @author feige<br />
 * @ClassName: EhcacheCacheManager <br/>
 * @Description: <br/>
 * @date: 2023/5/31 22:54<br/>
 */
public class EhcacheCacheManager extends AbstractCacheManager {
    private final CacheManager cacheManager;
    private long defaultCacheSize = 10000;

    public EhcacheCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    protected CacheGroup createGroup(String name) {
        return new EhcacheCacheGroup(name, this);
    }

    public CacheManager getCacheManager() {
        return this.cacheManager;
    }

    public long getDefaultCacheSize() {
        return defaultCacheSize;
    }

    public void setDefaultCacheSize(long defaultCacheSize) {
        this.defaultCacheSize = defaultCacheSize;
    }

    @Override
    public CacheGroup getGroup(Class<?> clz) {
        return new EhcacheCacheGroup(clz.getCanonicalName(), this);
    }
}
