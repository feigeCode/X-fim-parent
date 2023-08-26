package com.feige.framework.context;

import com.feige.framework.annotation.SpiComp;
import com.feige.framework.api.context.CompFactory;
import com.feige.framework.api.spi.NoSuchInstanceException;
import com.feige.utils.clazz.ClassUtils;

import java.util.List;

@SpiComp(interfaces = CompFactory.class)
public class SimpleCompFactory extends AbstractCompFactory {

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

    @Override
    public boolean isSupported(Class<?> type) {
        return !(type.isInterface() || ClassUtils.isAbstractClass(type));
    }
}
