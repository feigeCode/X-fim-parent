package com.feige.utils.convert;

public interface ObjectConvert<T, R> {
    
    R convertT(T t);
    
    T convertR(R r);
}
