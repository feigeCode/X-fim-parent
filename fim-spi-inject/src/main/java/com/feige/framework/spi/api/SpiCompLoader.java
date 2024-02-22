package com.feige.framework.spi.api;



import com.feige.framework.context.api.Lifecycle;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: SpiCompLoader <br/>
 * @Description: <br/>
 * @date: 2023/5/20 14:22<br/>
 */
public interface SpiCompLoader extends Lifecycle {

    void addIgnoreImpl(String... implNames);
    
    boolean isSpiComp(Class<?> cls);

    Class<?> get(String compName, Class<?> requireType) throws ClassNotFoundException;

    Class<?> getImplClassFormCache(String compName);
    
    String get(Class<?> requireType) throws ClassNotFoundException;
    
    List<String> getByType(Class<?> requireType) throws ClassNotFoundException;
    
    <T> List<T> loadSpiComps(Class<T> requireType);

    <T> T loadSpiComp(Class<T> requireType);
    
}
