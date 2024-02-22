package com.feige.api.cache;



/**
 * @author feige<br />
 * @ClassName: CacheManagerFactory <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:05<br/>
 */
public interface CacheManagerFactory {
    
    CacheManager create() throws Exception;
}
