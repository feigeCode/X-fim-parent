package com.feige.framework.api.context;


public interface CompNameGenerate extends Lifecycle {
    
    
    String generateName(Class<?> cls);
}
