package com.feige.fim.api;

public interface Event<T> {
    
    T getSource();
    
    int getType();
    
    Throwable getCause();
}
