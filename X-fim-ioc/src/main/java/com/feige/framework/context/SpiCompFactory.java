package com.feige.framework.context;

import com.feige.framework.annotation.SPI;
import com.feige.framework.api.context.CompFactory;
import com.feige.framework.api.spi.InstanceCreationException;
import com.feige.framework.api.spi.NoSuchInstanceException;
import com.feige.framework.api.spi.SpiCompProvider;
import com.feige.utils.common.AssertUtil;
import com.feige.utils.javassist.AnnotationUtils;
import com.feige.utils.spi.SpiScope;
import com.feige.utils.spi.annotation.SpiComp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpiComp(interfaces = CompFactory.class)
public class SpiCompFactory extends AbstractCompFactory {

    
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
        return type.isInterface() && AnnotationUtils.findAnnotation(type, SPI.class) != null;
    }

    protected  <T> T doGetInstance(String compName, Class<T> requireType, Object... args) {
        try {
            Object instance = getCompFromCache(compName);
            if (instance == null){
                Class<?> cls = this.getSpiCompLoader().get(compName, requireType);
                instance = createInstance(compName, cls, args);
                instance = getInstanceIfNecessary(instance, requireType);
                if (this.isGlobal(instance)){
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


    protected <T> T getInstanceIfNecessary(Object instance, Class<T> requireType) {
        if (!(instance instanceof SpiCompProvider) || SpiCompProvider.class.isAssignableFrom(requireType)){
            return (T) instance;
        }
        SpiCompProvider<T> spiCompProvider = (SpiCompProvider<T>) instance;
        return spiCompProvider.getInstance();
    }
    
    @Override
    public boolean isGlobal(Object obj){
        if (obj instanceof SpiCompProvider<?>){
            return Objects.equals(((SpiCompProvider<?>) obj).getScope(), SpiScope.GLOBAL);
        }
        return super.isGlobal(obj);
    }
}
