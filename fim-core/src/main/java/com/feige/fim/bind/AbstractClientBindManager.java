package com.feige.fim.bind;

import com.feige.fim.cache.CacheManager;
import lombok.Setter;

/**
 * @author feige<br />
 * @ClassName: AbstractClientBindManager <br/>
 * @Description: <br/>
 * @date: 2023/5/27 19:10<br/>
 */
public abstract class AbstractClientBindManager implements ClientBindManager {
    @Setter
    protected CacheManager cacheManager;

}
