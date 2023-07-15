package com.feige.fim.spi;

import com.feige.api.annotation.Spi;
import com.feige.api.spi.InstanceProvider;
import com.feige.api.spi.SpiLoader;
import com.feige.api.spi.SpiNotFoundException;
import com.feige.fim.ioc.InjectAnnotationInstancePostProcessor;
import com.feige.fim.ioc.InstancePostProcessor;
import com.feige.fim.lg.Loggers;
import com.feige.fim.utils.AssertUtil;
import com.feige.fim.utils.ClassUtils;
import com.feige.fim.utils.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractSpiLoader implements SpiLoader {

    protected static final Logger LOG = Loggers.LOADER;
    protected final Map<Class<?>, List<Object>> instanceCache = new ConcurrentHashMap<>();
    protected final Map<String, Object> singletonObjectCache = new ConcurrentHashMap<>();
    protected final Map<String, Object> instanceProviderObjectCache = new ConcurrentHashMap<>();
    protected final List<InstancePostProcessor> processors = new ArrayList<>();
    {
        processors.add(new InjectAnnotationInstancePostProcessor());
    }
    
    @Override
    public void register(Class<?> clazz, List<Object> instances) {
        AssertUtil.notNull(clazz, "class");
        AssertUtil.notNull(instances, "instances");
        this.instanceCache.put(clazz, instances);
        for (Object instance : instances) {
            String instanceName = getInstanceName(instance.getClass());
            this.singletonObjectCache.put(instanceName, instance);
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) throws SpiNotFoundException {
        AssertUtil.notBlank(key, "key");
        AssertUtil.notNull(clazz, "class");
        Object instance = this.singletonObjectCache.get(key);
        if (instance == null){
            List<Object> instanceList = loadClass(clazz);
            if (CollectionUtils.isEmpty(instanceList)) {
                throw new SpiNotFoundException(clazz);
            }
            instance = this.singletonObjectCache.get(key);
        }
        if (instance == null){
            throw new SpiNotFoundException(clazz, key);
        }
        return clazz.cast(instance);
    }

    @Override
    public <T> T getFirst(Class<T> clazz) throws SpiNotFoundException {
        AssertUtil.notNull(clazz, "class");
        List<Object> instanceList = loadClass(clazz);
        if (CollectionUtils.isEmpty(instanceList)){
            throw new SpiNotFoundException(clazz);
        }
        return clazz.cast(instanceList.get(0));
    }

    @Override
    public <T> List<T> getAll(Class<T> clazz) throws SpiNotFoundException {
        AssertUtil.notNull(clazz, "class");
        List<Object> instanceList = loadClass(clazz);
        if (instanceList == null){
            throw new SpiNotFoundException(clazz);
        }
        return instanceList.stream()
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

   
    protected boolean checkInstance(Object instance) {
        return checkInstance(instance.getClass());
    }

    protected boolean checkInstance(Class<?> clazz) {
        boolean annotationPresent = clazz.isAnnotationPresent(Spi.class);
        if (!annotationPresent){
            LOG.warn("class = {}, No @Spi", clazz.getName());
            return false;
        }
        String instanceName = getInstanceName(clazz);
        boolean isDuplication = this.singletonObjectCache.containsKey(instanceName);
        if (isDuplication){
            LOG.error("instance name [{}] is duplication", instanceName);
            return false;
        }
        return true;
    }

    private  List<Object> loadClass(Class<?> clazz){
        List<Object> instanceList = instanceCache.get(clazz);
        if (instanceList == null){
            synchronized (instanceCache){
                instanceList = instanceCache.get(clazz);
                if(instanceList == null){
                    load(clazz);
                    instanceList = instanceCache.get(clazz);
                }
            }
        }
        if (CollectionUtils.isEmpty(instanceList)){
            throw new SpiNotFoundException(clazz);
        }
        return instanceList;
    }

    private void load(Class<?> clazz) {
        if (clazz.isInterface() || ClassUtils.isAbstractClass(clazz)){
            doLoadInstance(clazz);
        }else {
            Object instance = createInstance(clazz);
            register(clazz, Collections.singletonList(instance));
        }
    }
    
    protected Object createInstance(Class<?> clazz){
        if (checkInstance(clazz)){
            try {
                Object instance = clazz.newInstance();
                String instanceName = getInstanceName(clazz);
                applyBeanPostProcessorsBeforeInitialization(instance, instanceName);
                return instance;
            } catch (Exception e) {
                LOG.error("instance error:" , e);
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    protected Object instanceProviderHandle(String key, Object instance) {
        if (instance instanceof InstanceProvider) {
            Object realInstance = instanceProviderObjectCache.get(key);
            if (realInstance == null){
                synchronized (instanceProviderObjectCache){
                    realInstance = instanceProviderObjectCache.get(key);
                    if (realInstance == null){
                        realInstance = ((InstanceProvider<?>) instance).get();
                        instanceProviderObjectCache.put(key, instance);
                    }
                }
            }
            return realInstance;
        }
        return instance;
    }

    protected Object applyBeanPostProcessorsBeforeInitialization(Object instance,  String instanceName) throws Exception {
        for (InstancePostProcessor processor : processors) {
            processor.postProcessBeforeInitialization(instance, instanceName);
        }
        return instance;
    }


    protected Object applyBeanPostProcessorsAfterInitialization(Object instance,  String instanceName) throws Exception {
        for (InstancePostProcessor processor : processors) {
            processor.postProcessAfterInitialization(instance, instanceName);
        }
        return instance;
    }
    
    protected String getInstanceName(Class<?> clazz){
        Spi spiAnnotation = clazz.getAnnotation(Spi.class);
        String value = spiAnnotation.value();
        if (StringUtil.isNotBlank(value)){
            return value;
        }
        return StringUtil.capitalize(clazz.getSimpleName());
    }
    
    protected abstract void doLoadInstance(Class<?> loadClass);


    
}
