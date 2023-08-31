package com.feige.framework.context.api;


public interface CompNameGenerate extends Lifecycle {
    
    
    String generateName(Class<?> cls);
}
