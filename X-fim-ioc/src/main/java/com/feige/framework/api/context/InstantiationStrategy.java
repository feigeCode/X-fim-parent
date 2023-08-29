package com.feige.framework.api.context;


public interface InstantiationStrategy extends Lifecycle{
    
    <T> T instantiate(Class<T> cl, Object... args);
}
