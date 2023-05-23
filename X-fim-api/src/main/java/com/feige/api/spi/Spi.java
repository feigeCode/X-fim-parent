package com.feige.api.spi;

public interface Spi {
    /**
     * get spi key
     * @return
     */
    String getKey();

    /**
     * Whether it is primary
     * @return is primary
     */
    default boolean primary() {
        return false;
    }

    /**
     * order
     * @return order number
     */
    default int order() {
        return 0;
    }
}
