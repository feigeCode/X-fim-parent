package com.feige.fim.spi;

public class SpiNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3160453149606778709L;

    private final Class<?> clazz;
    private String key;

    public SpiNotFoundException(Class<?> clazz) {
        super("No implementation of interface [" + clazz.getCanonicalName() + "] was found.");
        this.clazz = clazz;
    }

    public SpiNotFoundException(Class<?> clazz, String key) {
        super("No implementation of [" + key + "] was found by the interface [" + clazz.getCanonicalName() + "].");
        this.clazz = clazz;
        this.key = key;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getKey() {
        return key;
    }


}
