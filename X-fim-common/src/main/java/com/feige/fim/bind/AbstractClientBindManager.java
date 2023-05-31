package com.feige.fim.bind;

import com.feige.api.bind.ClientBindManager;
import com.feige.api.cache.CacheManager;

/**
 * @author feige<br />
 * @ClassName: AbstractClientBindManager <br/>
 * @Description: <br/>
 * @date: 2023/5/27 19:10<br/>
 */
public abstract class AbstractClientBindManager implements ClientBindManager {
    protected CacheManager cacheManager;

    public AbstractClientBindManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
