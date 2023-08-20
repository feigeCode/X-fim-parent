package com.feige.cache.local;

import com.feige.framework.annotation.SpiComp;
import com.feige.api.cache.CacheManager;
import com.feige.api.cache.CacheManagerFactory;
import com.feige.framework.api.spi.InstanceProvider;


@SpiComp(value="local", interfaces = InstanceProvider.class)
public class LocalCacheManagerFactory implements CacheManagerFactory, InstanceProvider<CacheManager> {
    @Override
    public CacheManager create() {
        return new LocalCacheManager();
    }

    @Override
    public CacheManager getInstance() {
        return create();
    }

    @Override
    public Class<CacheManager> getType() {
        return CacheManager.class;
    }
}
