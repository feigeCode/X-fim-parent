package com.feige.framework.context;

import com.feige.framework.context.api.CompFactory;
import com.feige.framework.spi.api.InstanceCreationException;
import com.feige.framework.spi.api.NoSuchInstanceException;
import com.feige.framework.spi.api.SpiCompProvider;
import com.feige.utils.common.AssertUtil;
import com.feige.utils.spi.SpiScope;
import com.feige.utils.spi.annotation.SpiComp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@SpiComp(interfaces = CompFactory.class)
public class SpiCompFactory extends AbstractCompFactory {

    protected final Map<String, SpiCompProvider<?>> providerObjectCache = new ConcurrentHashMap<>(16);


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
        Object instance =  super.getCompFromCache(compName);
        if (instance == null){
            instance = providerObjectCache.get(compName);
        }
        return instance;
    }

    protected  <T> T doGetInstance(String compName, Class<T> requireType, Object... args) {
        try {
            boolean isRegistered = true;
            Object instance = getCompFromCache(compName);
            if (instance == null){
                Class<?> cls = this.getSpiCompLoader().get(compName, requireType);
                if (this.getSpiCompLoader().getImplClassFormCache(compName) == null) {
                    return null;
                }
                instance = createInstance(compName, cls, args);
                isRegistered = false;
            }
            if (instance != null){
                boolean global = false;
                if (!isRegistered){
                    global = this.isGlobal(compName, instance) || this.isModule(compName, instance);
                }
                instance = getInstanceIfNecessary(compName, instance, requireType);
                if (global){
                    getCompRegistry().register(compName, instance);
                }
            }
            return (T) instance;
        } catch (Throwable e) {
            throw new InstanceCreationException(e, requireType);
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
            for (String compName : compNames) {
                T t = doGetInstance(compName, type);
                ts.add(t);
            }
            return ts;
        } catch (Throwable e) {
            throw new InstanceCreationException(e, type);
        }
    }


    protected <T> T getInstanceIfNecessary(String compName, Object instance, Class<T> requireType) {
        if (!(instance instanceof SpiCompProvider) || SpiCompProvider.class.isAssignableFrom(requireType)){
            return (T) instance;
        }
        SpiCompProvider<T> spiCompProvider = (SpiCompProvider<T>) instance;
        providerObjectCache.put(compName, spiCompProvider);
        return spiCompProvider.getInstance();
    }
    
    @Override
    public boolean isGlobal(String compName, Object obj){
        if (obj instanceof SpiCompProvider<?>){
            return Objects.equals(((SpiCompProvider<?>) obj).getScope(), SpiScope.GLOBAL);
        }
        return super.isGlobal(compName, obj);
    }


    @Override
    public boolean isModule(String compName, Object obj){
        if (obj instanceof SpiCompProvider<?>){
            return Objects.equals(((SpiCompProvider<?>) obj).getScope(), SpiScope.MODULE);
        }
        return super.isModule(compName, obj);
    }
    
    
}
