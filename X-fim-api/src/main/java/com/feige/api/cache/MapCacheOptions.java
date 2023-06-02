package com.feige.api.cache;

import java.time.Duration;

/**
 * @author feige<br />
 * @ClassName: MapCacheOptions <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:19<br/>
 */
public class MapCacheOptions<K, V> {
    private int writeBehindBatchSize = 50;
    private int writeBehindDelay = 1000;
    private Duration timeToLive;
    private long maximumSize;
    private MapCacheLoader<K, V> loader;

    public static <K, V> MapCacheOptions<K, V> defaults() {
        return new MapCacheOptions<K, V>();
    }
    public static <K, V> MapCacheOptions<K, V> fromLoader(MapCacheLoader<K, V> loader) {
        MapCacheOptions<K,V> options= new MapCacheOptions<K, V>();
        options.loader = loader;
        return options;
    }
    public MapCacheOptions<K, V> loader(MapCacheLoader<K, V> loader) {
        this.loader = loader;
        return this;
    }

    public MapCacheLoader<K, V> getLoader() {
        return loader;
    }

    public MapCacheOptions<K, V> writeBehindBatchSize(int writeBehindBatchSize) {
        this.writeBehindBatchSize = writeBehindBatchSize;
        return this;
    }

    public int getWriteBehindBatchSize() {
        return writeBehindBatchSize;
    }

    public MapCacheOptions<K, V> writeBehindDelay(int writeBehindDelay) {
        this.writeBehindDelay = writeBehindDelay;
        return this;
    }

    public int getWriteBehindDelay() {
        return writeBehindDelay;
    }

    public MapCacheOptions<K, V> expire(Duration timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }

    public MapCacheOptions<K, V> maximumSize(long maximumSize) {
        this.maximumSize = maximumSize;
        return this;
    }

    public long getMaximumSize() {
        return maximumSize;
    }

    public Duration getTimeToLive() {
        return timeToLive;
    }
}
