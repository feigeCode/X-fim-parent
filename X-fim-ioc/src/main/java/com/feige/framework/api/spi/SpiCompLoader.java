package com.feige.framework.api.spi;



import java.util.List;

/**
 * @author feige<br />
 * @ClassName: SpiCompLoader <br/>
 * @Description: <br/>
 * @date: 2023/5/20 14:22<br/>
 */
public interface SpiCompLoader {

    void addIgnoreImpl(String... implNames);
    
    boolean isSpiComp(Class<?> cls);
    
    boolean isPrototype(String compName);
    
    boolean isSingleton(String compName);

    <T> Class<T> get(String compName, Class<T> requireType) throws ClassNotFoundException;

    <T> Class<T> get(Class<T> requireType) throws ClassNotFoundException;
    
    <T> List<Class<T>> getByType(Class<T> requireType) throws ClassNotFoundException;

    <T> List<Class<SpiCompProvider<T>>> getByProviderType(Class<T> requireType);
    
    List<String> getCompNames(Class<?> requireType) throws ClassNotFoundException;

    <T> List<T> loadSpiComps(Class<T> requireType);

    <T> T loadSpiComp(Class<T> requireType);
    
}
