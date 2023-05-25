package com.feige.api.cache;

/**
 * @author feige<br />
 * @ClassName: CacheManager <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:04<br/>
 */
public interface CacheManager {
    CacheGroup getGroup(String name);

    CacheGroup getGroup(Class<?> clz);
}
