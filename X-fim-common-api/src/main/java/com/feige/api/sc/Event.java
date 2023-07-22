package com.feige.api.sc;

public interface Event<T> {
    
    T getSource();
    
    int getType();
    
    Throwable getCause();
}
