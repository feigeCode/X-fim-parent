package com.feige.cache.local;

import com.feige.framework.annotation.SpiComp;
import com.feige.api.cache.CacheManager;
import com.feige.api.cache.CacheManagerFactory;
import com.feige.framework.api.spi.SpiCompProvider;


@SpiComp(value="local", interfaces = SpiCompProvider.class, provideTypes = CacheManager.class)
public class LocalCacheManagerFactory implements CacheManagerFactory, SpiCompProvider<CacheManager> {
    @Override
    public CacheManager create() {
        return new LocalCacheManager();
    }

    @Override
    public CacheManager getInstance() {
        return create();
    }
    
}
