package com.feige.api.sc;

import java.util.Map;

public interface Callback<T> {
    
    void call(T t, Map<String, Object> args);
}
