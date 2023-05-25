package com.feige.api.cache;

/**
 * @author feige<br />
 * @ClassName: LocalMapCacheOptions <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:19<br/>
 */
public class LocalMapCacheOptions<K, V> extends MapCacheOptions<K, V> {
    private int cacheSize;
    private long maxIdleInMillis;

    public static <K, V> LocalMapCacheOptions<K, V> defaults() {
        return new LocalMapCacheOptions<K, V>();
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void cacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }


    public long maxIdleInMillis() {
        return maxIdleInMillis;
    }

    public void setMaxIdleInMillis(long maxIdleInMillis) {
        this.maxIdleInMillis = maxIdleInMillis;
    }
}