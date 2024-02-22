package com.feige.api.serialize;



public interface Serializer {


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
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    /**
     * type
     * @return
     */
    byte getType();


}
