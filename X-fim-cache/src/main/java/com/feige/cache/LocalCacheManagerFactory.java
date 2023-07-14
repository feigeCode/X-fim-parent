package com.feige.cache;

import com.feige.api.annotation.Spi;
import com.feige.api.cache.CacheManager;
import com.feige.api.cache.CacheManagerFactory;

@Spi("local")
public class LocalCacheManagerFactory implements CacheManagerFactory {
    @Override
    public CacheManager create() {
        return new LocalCacheManager();
    }
    
}
