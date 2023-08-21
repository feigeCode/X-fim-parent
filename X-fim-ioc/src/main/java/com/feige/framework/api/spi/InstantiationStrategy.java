package com.feige.framework.api.spi;

import com.feige.utils.clazz.ClassUtils;

public interface InstantiationStrategy {
    
    <T> T instantiate(Class<T> cl, Object... args);
    
    
    default Object instantiate(String className, Object... args) {
        try {
            Class<?> cl = ClassUtils.forName(className, this.getClass().getClassLoader());
            return instantiate(cl, args);
        } catch (ClassNotFoundException e) {
           throw new RuntimeException(e);
        }
    }
}
