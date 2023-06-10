package com.feige.cache;

import com.feige.api.cache.CacheManager;
import com.feige.api.cache.CacheManagerFactory;

public class LocalCacheManagerFactory implements CacheManagerFactory {
    @Override
    public CacheManager create() {
        return new LocalCacheManager();
    }

    @Override
    public String getKey() {
        return "local";
    }
}
