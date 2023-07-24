package com.feige.cache;

import com.feige.annotation.SpiComp;
import com.feige.api.cache.CacheManager;
import com.feige.api.cache.CacheManagerFactory;

@SpiComp("local")
public class LocalCacheManagerFactory implements CacheManagerFactory {
    @Override
    public CacheManager create() {
        return new LocalCacheManager();
    }
    
}
