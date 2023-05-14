package com.feige.api.sc;

import java.net.InetSocketAddress;


public interface Server extends Endpoint {


    /**
     * is bound.
     *
     * @return bound
     */
    boolean isBound();
    /**
     *bind
     * @param bindAddress
     */
    void bind(InetSocketAddress bindAddress);

}
