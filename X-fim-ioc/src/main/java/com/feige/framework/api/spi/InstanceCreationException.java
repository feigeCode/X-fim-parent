package com.feige.framework.api.spi;

public class InstanceCreationException extends RuntimeException {
    private final Class<?> clazz;

    public InstanceCreationException(Throwable e, Class<?> clazz) {
        super(clazz.getName() + " create failure", e);
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
