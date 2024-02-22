package com.feige.api.msg;

import java.io.Serializable;

public interface Msg extends Serializable {

    /**
     * serialization
     * @return byte[]
     */
    byte[] serialize();


    /**
     * deserialization
     * @param bytes byte[]
     */
    void deserialize(byte[] bytes);
}
