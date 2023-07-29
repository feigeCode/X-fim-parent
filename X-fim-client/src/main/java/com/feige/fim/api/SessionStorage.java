package com.feige.fim.api;

public interface SessionStorage {
    void setItem(String key, String value);
    
    String getItem(String key);
    
    String removeItem(String key);
    
    void clear();
}
