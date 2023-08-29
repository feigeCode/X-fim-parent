package com.feige.framework.context;

import com.feige.framework.api.context.LifecycleAdapter;
import com.feige.utils.spi.annotation.SpiComp;
import com.feige.framework.api.context.InstantiationStrategy;
import com.feige.utils.clazz.ReflectionUtils;

@SpiComp(interfaces = InstantiationStrategy.class)
public class SimpleInstantiateStrategy  extends LifecycleAdapter implements InstantiationStrategy {
    @Override
    public <T> T instantiate(Class<T> cl, Object... args) {
        try {
            return ReflectionUtils.accessibleConstructor(cl).newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
