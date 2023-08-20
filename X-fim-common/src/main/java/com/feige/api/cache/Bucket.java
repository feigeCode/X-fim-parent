package com.feige.api.cache;

import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public interface Bucket<V extends Serializable> extends Cacheable {
    V get();

    void set(V val);
    
    void set(V val, long expiryTime, TimeUnit unit);

    V getAndDelete();
    
    boolean isExpired();
    
    void setEx(Duration duration);
}
