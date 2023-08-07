package com.feige.cache;

import com.feige.api.cache.AbstractCacheable;
import com.feige.api.cache.Bucket;

import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

public class LocalBucket<V extends Serializable> extends AbstractCacheable implements Bucket<V> {

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    private final AtomicReference<V> valRef = new AtomicReference<>();
    private volatile long expiryTime = -1;

    public LocalBucket(String name) {
        super(name);
    }

    @Override
    public V get() {
        readLock.lock();
        try {
            checkExpired();
            return this.valRef.get();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void set(V val) {
        writeLock.lock();
        try {
            checkExpired();
            this.valRef.set(val);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void set(V val, long expiryTime, TimeUnit unit) {
        writeLock.lock();
        try {
            checkExpired();
            this.valRef.set(val);
            this.expiryTime = System.currentTimeMillis() + unit.toMillis(expiryTime);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public V getAndDelete() {
        writeLock.lock();
        try {
            checkExpired();
            V v = this.valRef.get();
            this.valRef.set(null);
            return v;
        } finally {
            writeLock.unlock();
        }
    }

    private void checkExpired() {
        if (isExpired()) {
            throw new IllegalStateException("The current cache has expired.");
        }
    }

    @Override
    public boolean isExpired() {
        return this.expiryTime != -1 && this.expiryTime > System.currentTimeMillis();
    }

    @Override
    public void setEx(Duration duration) {
        writeLock.lock();
        try {
            this.expiryTime = System.currentTimeMillis() + duration.toMillis();
        } finally {
            writeLock.unlock();
        }
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
