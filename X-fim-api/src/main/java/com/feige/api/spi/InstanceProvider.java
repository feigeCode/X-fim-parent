package com.feige.api.spi;

public interface InstanceProvider<T> {
    
    T get();
}
