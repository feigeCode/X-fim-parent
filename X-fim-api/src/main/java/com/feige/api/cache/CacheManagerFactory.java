package com.feige.api.cache;

import com.feige.api.spi.Spi;

/**
 * @author feige<br />
 * @ClassName: CacheManagerFactory <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:05<br/>
 */
public interface CacheManagerFactory extends Spi {
    
    CacheManager create() throws Exception;
}
