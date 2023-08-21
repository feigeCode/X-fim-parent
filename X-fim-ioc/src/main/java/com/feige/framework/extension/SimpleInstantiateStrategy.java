package com.feige.framework.extension;

import com.feige.framework.api.spi.InstantiationStrategy;
import com.feige.utils.clazz.ReflectionUtils;

public class SimpleInstantiateStrategy implements InstantiationStrategy {
    @Override
    public <T> T instantiate(Class<T> cl, Object... args) {
        try {
            return ReflectionUtils.accessibleConstructor(cl).newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
