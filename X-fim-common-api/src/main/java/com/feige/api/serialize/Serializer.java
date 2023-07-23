package com.feige.api.serialize;


import com.feige.api.msg.Msg;

public interface Serializer {


    /**
     * serialization
     * @param obj object
     * @return byte[]
     */
    byte[] serialize(Msg obj);


    /**
     * deserialization
     * @param bytes byte[]
     * @return object
     */
    <T extends Msg> T deserialize(Class<T> clazz, byte[] bytes);

    /**
     * type
     * @return
     */
    byte getType();


}
