package com.feige.framework.api.spi;

@FunctionalInterface
public interface ObjectFactory<T> {


	T getObject() throws RuntimeException;

}
