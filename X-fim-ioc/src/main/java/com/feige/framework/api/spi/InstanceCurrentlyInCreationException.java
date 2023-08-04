package com.feige.framework.api.spi;

public class InstanceCurrentlyInCreationException extends RuntimeException {
    private final Class<?> clazz;

    public InstanceCurrentlyInCreationException(Class<?> clazz) {
        super(clazz.getName() +  " Creating");
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
    
}
