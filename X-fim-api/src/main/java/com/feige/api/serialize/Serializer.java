package com.feige.api.serialize;

import com.feige.api.spi.Spi;

public interface Serializer extends Spi {


    /**
     * serialization
     * @param obj object
     * @return byte[]
     */
    byte[] serialize(Object obj);


    /**
     * deserialization
     * @param bytes byte[]
     * @return object
     */
    Object deserialize(byte[] bytes);
}
