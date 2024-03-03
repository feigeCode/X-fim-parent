package com.feige.framework.factory;

import com.feige.framework.factory.api.CompFactory;
import com.feige.framework.spi.api.InstanceCreationException;
import com.feige.framework.spi.api.InstanceCurrentlyInCreationException;
import com.feige.framework.spi.api.NoSuchInstanceException;
import com.feige.framework.spi.api.SpiCompLoader;
import com.feige.utils.common.AssertUtil;
import com.feige.utils.spi.annotation.SPI;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@SPI(interfaces = CompFactory.class)
public class SpiCompFactory extends AbstractCompFactory {
    private final Map<String, Set<String>> moduleCurrentlyInCreation = new ConcurrentHashMap<>(16);
    protected final Map<String, Object> providerObjectCache = new ConcurrentHashMap<>(16);
    

    @Override
    public <T> T get(String compName, Class<T> requireType, Object... args) throws NoSuchInstanceException {
        AssertUtil.notBlank(compName, "compName");
        AssertUtil.notNull(requireType, "requireType");
        return doGetInstance(compName, requireType, args);
    }


    @Override
    public <T> T get(Class<T> type, Object... args) throws NoSuchInstanceException {
        AssertUtil.notNull(type, "type");
        return doGetInstance(type, args);
    }

    @Override
    public <T> List<T> getByType(Class<T> type) throws NoSuchInstanceException {
        AssertUtil.notNull(type, "type");
        return doGetInstances(type);
    }


    @Override
    protected Object getCompFromCache(String compName) {
        Object instance = providerObjectCache.get(compName);
        if (instance == null){
            instance = super.getCompFromCache(compName);
        }
        return instance;
    }

    protected  <T> T doGetInstance(String compName, Class<T> requireType, Object... args) {
        try {
            Object instance = getCompFromCache(compName);
            if (instance == null){
                Class<?> cls = getImplClass(compName, requireType);
                if (this.isGlobal(requireType, compName)){
                    instance = createInstance(requireType, compName, args);
                }else {
                    checkModuleCurrentlyInCreation(compName, requireType, cls);
                    instance = createOneOrModuleInstance(requireType, compName, args);
                }
                if (this.isGlobal(requireType, compName) || this.isModule(requireType, compName)){
                    getCompRegistry().register(compName, instance);
                }
            }
            return (T) instance;
        } catch (Throwable e) {
            throw new InstanceCreationException(e, requireType);
        }finally {
            Set<String> currentlyInCreation = moduleCurrentlyInCreation.get(compName);
            if (CollectionUtils.isNotEmpty(currentlyInCreation)){
                currentlyInCreation.remove(compName);
            }
        }
    }
    
    private Class<?> getImplClass(String compName, Class<?> requireType) throws ClassNotFoundException {
        SpiCompLoader spiCompLoader = this.getSpiCompLoader();
        Class<?> cls = spiCompLoader.get(requireType, compName);
        // 如果非Module类型，当前loader中没有，交给父模块去创建
        if (spiCompLoader.getImplClassFormCache(compName) == null && !this.isModule(requireType, compName)) {
            return null;
        }
        return cls;
    }
    
    private void checkModuleCurrentlyInCreation(String compName, Class<?> requireType, Class<?> cls){
        if (this.isModule(requireType, compName)){
            Set<String> currentlyInCreation = moduleCurrentlyInCreation.computeIfAbsent(applicationContext.moduleName(), k -> new ConcurrentSkipListSet<>());
            if (currentlyInCreation.contains(compName) && !currentlyInCreation.add(compName)){
                throw new InstanceCurrentlyInCreationException(cls);
            }
        }
    }


    protected   <T> T doGetInstance(Class<T> requireType, Object... args) {
        try {
            String compName = this.getSpiCompLoader().get(requireType);
            if (compName == null){
                throw new NoSuchInstanceException(requireType);
            }
            return doGetInstance(compName, requireType, args);
        }catch (Throwable e){
            throw new InstanceCreationException(e, requireType);
        }
    }


    protected <T> List<T> doGetInstances(Class<T> type){
        try {
            List<T> ts = new ArrayList<>();
            List<String> compNames = this.getSpiCompLoader().getByType(type);
            if (compNames == null){
                return ts;
            }
            for (String compName : compNames) {
                T t = doGetInstance(compName, type);
                ts.add(t);
            }
            return ts;
        } catch (Throwable e) {
            throw new InstanceCreationException(e, type);
        }
    }
}
