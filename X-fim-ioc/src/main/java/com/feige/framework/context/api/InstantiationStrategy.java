package com.feige.framework.context.api;


public interface InstantiationStrategy extends Lifecycle{
    
    <T> T instantiate(Class<T> cl, Object... args);
}
