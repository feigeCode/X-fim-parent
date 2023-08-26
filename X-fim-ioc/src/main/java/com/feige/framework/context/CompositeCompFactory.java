package com.feige.framework.context;

import com.feige.framework.api.context.CompFactory;
import com.feige.framework.api.context.LifecycleAdapter;
import com.feige.framework.api.spi.NoSuchInstanceException;

import java.util.List;

public class CompositeCompFactory extends LifecycleAdapter implements CompFactory {
    @Override
    public void register(String instanceName, Object instance) {
        
    }

    @Override
    public <T> T get(String compName, Class<T> requireType, Object... args) throws NoSuchInstanceException {
        return null;
    }

    @Override
    public <T> T get(Class<T> requireType, Object... args) throws NoSuchInstanceException {
        return null;
    }

    @Override
    public <T> List<T> getByType(Class<T> requireType) throws NoSuchInstanceException {
        return null;
    }
}
