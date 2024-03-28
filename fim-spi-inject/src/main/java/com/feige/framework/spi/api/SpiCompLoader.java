package com.feige.framework.spi.api;



import com.feige.framework.context.api.Lifecycle;
import com.feige.framework.instantiate.api.InstantiationStrategy;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: SpiCompLoader <br/>
 * @Description: <br/>
 * @date: 2023/5/20 14:22<br/>
 */
public interface SpiCompLoader extends Lifecycle {

    void addIgnoreImpl(String... implNames);

    Class<?> get(Class<?> requireType, String compName) throws ClassNotFoundException;

    Class<?> getImplClassFormCache(String compName);
    
    String getCompNameFromCache(Class<?> implClass) throws ClassNotFoundException;
    
    List<String> getCompNamesByType(Class<?> requireType) throws ClassNotFoundException;
    
    <T> List<T> loadSpiComps(Class<T> requireType);

    <T> T loadSpiComp(Class<T> requireType, String compName, Object... args);

    <T> T loadSpiComp(Class<T> requireType, Object... args);

    InstantiationStrategy getInstantiationStrategy();
    
}
