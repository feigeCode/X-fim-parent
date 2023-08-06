package com.feige.cache;

import com.feige.api.cache.AbstractCacheable;
import com.feige.api.cache.Bucket;

import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class LocalBucket<V extends Serializable> extends AbstractCacheable implements Bucket<V> {
    
    
    private AtomicReference<V> valRef = new AtomicReference<>();
    
    private volatile long expiryTime = -1;
    
    public LocalBucket(String name) {
        super(name);
    }

    @Override
    public V get() {
        checkExpired();
        return this.valRef.get();
    }

    @Override
    public void set(V val) {
        checkExpired();
       this.valRef.set(val);
    }

    @Override
    public void set(V val, long expiryTime, TimeUnit unit) {
        checkExpired();
        set(val);
        this.expiryTime = System.currentTimeMillis() + unit.convert(expiryTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public V getAndDelete() {
        checkExpired();
        V v = this.valRef.get();
        this.valRef.set(null);
        return v;
    }
    
    private void checkExpired(){
        if (isExpired()) {
            this.valRef = null;
            throw new IllegalStateException("The current cache has expired.");
        }
    }

    @Override
    public boolean isExpired() {
        return this.expiryTime == -1 || this.expiryTime > System.currentTimeMillis() ;
    }

    @Override
    public void setEx(Duration duration) {
        this.expiryTime = System.currentTimeMillis() + duration.toMillis();
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
