package com.feige.cache;

import com.feige.api.cache.AbstractCacheable;
import com.feige.api.cache.Bucket;
import org.redisson.api.RBucket;

import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class RedisBucket<V extends Serializable> extends AbstractCacheable implements Bucket<V> {
    private final RBucket<V> rBucket;
    public RedisBucket(String name, RBucket<V> rBucket) {
        super(name);
        this.rBucket = rBucket;
    }

    @Override
    public V get() {
        return rBucket.get();
    }

    @Override
    public void set(V val) {
        rBucket.set(val);
    }

    @Override
    public void set(V val, long expiryTime, TimeUnit unit) {
        rBucket.set(val, expiryTime, unit);
    }

    @Override
    public V getAndDelete() {
        return rBucket.getAndDelete();
    }

    @Override
    public boolean isExpired() {
        long expireTime = rBucket.getExpireTime();
        return expireTime > 0 || expireTime == -1;
    }

    @Override
    public void setEx(Duration duration) {
        rBucket.expire(duration);
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
