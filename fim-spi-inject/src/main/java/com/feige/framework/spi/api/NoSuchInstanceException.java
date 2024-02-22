package com.feige.framework.spi.api;

public class NoSuchInstanceException extends RuntimeException {

    private static final long serialVersionUID = -3160453149606778709L;

    private final Class<?> clazz;
    private String instanceName;

    public NoSuchInstanceException(Class<?> clazz) {
        super("No implementation of interface [" + clazz.getCanonicalName() + "] was found.");
        this.clazz = clazz;
    }

    public NoSuchInstanceException(Class<?> clazz, String instanceName) {
        super("No implementation of [" + instanceName + "] was found by the interface [" + clazz.getCanonicalName() + "].");
        this.clazz = clazz;
        this.instanceName = instanceName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getInstanceName() {
        return instanceName;
    }


}
