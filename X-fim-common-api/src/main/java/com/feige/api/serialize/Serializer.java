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
    Object deserialize(byte[] bytes);


}
