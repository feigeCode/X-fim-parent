package com.feige.api.spi;

public interface Spi {
    /**
     * get spi key
     * @return
     */
    String getKey();

    /**
     * order
     * @return order number
     */
    default int order() {
        return 0;
    }
}
