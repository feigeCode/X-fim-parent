package com.feige.framework.instantiate.api;


import com.feige.framework.context.api.Lifecycle;

public interface InstantiationStrategy extends Lifecycle {
    
    <T> T instantiate(Class<T> cl, Object... args);
}
