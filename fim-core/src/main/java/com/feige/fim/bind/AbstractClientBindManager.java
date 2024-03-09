package com.feige.fim.bind;

import com.feige.fim.cache.CacheManager;
import com.feige.framework.annotation.Inject;

/**
 * @author feige<br />
 * @ClassName: AbstractClientBindManager <br/>
 * @Description: <br/>
 * @date: 2023/5/27 19:10<br/>
 */
public abstract class AbstractClientBindManager implements ClientBindManager {
    @Inject
    protected CacheManager cacheManager;

}
