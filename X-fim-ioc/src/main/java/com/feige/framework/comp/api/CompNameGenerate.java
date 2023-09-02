package com.feige.framework.comp.api;


import com.feige.framework.context.api.Lifecycle;

public interface CompNameGenerate extends Lifecycle {
    
    
    String generateName(Class<?> cls);
}
