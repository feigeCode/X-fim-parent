package com.feige.framework.context;

import com.feige.framework.api.spi.InstanceCreationException;
import com.feige.framework.api.spi.NoSuchInstanceException;
import com.feige.framework.api.spi.SpiCompProvider;
import com.feige.utils.clazz.ClassUtils;
import com.feige.utils.common.AssertUtil;

import java.util.ArrayList;
import java.util.List;

public class SimpleSpiCompFactory extends AbstractCompFactory {
    

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
    public boolean isSupported(Class<?> type) {
        return type.isInterface() || ClassUtils.isAbstractClass(type);
    }

    private   <T> T doGetInstance(Class<T> requireType, Object... args) {
        try {
            List<Class<SpiCompProvider<T>>> providerClasses = this.getSpiCompLoader().getByProviderType(requireType);
            
            Class<T> cls = this.getSpiCompLoader().get(requireType);
            String compName = getCompNameGenerate().generateName(cls);
            Object singleton = getSingleton(compName);
            if (singleton == null){
                singleton = createInstance(compName, cls, args);
            }
            return (T) singleton;
        }catch (Throwable e){
            throw new InstanceCreationException(e, requireType);
        }
    }
    
    @Override
    protected Object getSingleton(String compName) {
        Object singleton = null;
        if (getSpiCompLoader().isSingleton(compName)) {
            singleton =  super.getSingleton(compName);
        }
        return singleton;
    }

    private <T> T doGetInstance(String compName, Class<T> requireType, Object... args) {
        try {
            Object singleton = getSingleton(compName);
            if (singleton == null){
                Class<T> cls = this.getSpiCompLoader().get(compName, requireType);
                singleton = createInstance(compName, cls, args);
            }
            return (T) singleton;
        } catch (Throwable e) {
            throw new InstanceCreationException(e, requireType);
        }
    }
    

    private <T> List<T> doGetInstances(Class<T> type){
        try {
            List<T> ts = new ArrayList<>();
            List<Class<T>> classes = this.getSpiCompLoader().getByType(type);
            for (Class<T> cls : classes) {
                String compName = getCompNameGenerate().generateName(cls);
                T t = doGetInstance(compName, type);
                ts.add(t);
            }
            return ts;
        } catch (Throwable e) {
            throw new InstanceCreationException(e, type);
        }
    }


    private <T> T getCompIfNecessary(String compName, Object instance, Class<T> requireType) {
        if (instance instanceof SpiCompProvider && !SpiCompProvider.class.isAssignableFrom(requireType)) {
            SpiCompProvider<T> spiCompProvider = (SpiCompProvider<T>) instance;
            if (!isSingleton(instance)){
                return spiCompProvider.getInstance();
            }
            Object realInstance = instanceProviderObjectCache.get(compName);
            if (realInstance == null){
                synchronized (instanceProviderObjectCache){
                    realInstance = instanceProviderObjectCache.get(compName);
                    if (realInstance == null){
                        realInstance = spiCompProvider.getInstance();
                        if (spiCompProvider.isSingleton()){
                            instanceProviderObjectCache.put(compName, realInstance);
                        }
                    }
                }
            }
            return (T) realInstance;
        }
        return (T) instance;
    }
    
    @Override
    protected boolean isSingleton(Object obj){
        if (obj instanceof SpiCompProvider<?>){
            return ((SpiCompProvider<?>) obj).isSingleton();
        }
        return super.isSingleton(obj);
    }
}
