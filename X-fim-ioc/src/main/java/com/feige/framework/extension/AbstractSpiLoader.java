package com.feige.framework.extension;

import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.framework.annotation.Value;
import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.ApplicationContextAware;
import com.feige.framework.api.context.EnvironmentAware;
import com.feige.framework.api.context.LifecycleAdapter;
import com.feige.framework.api.context.SpiLoaderAware;
import com.feige.framework.order.OrderComparator;
import com.feige.framework.api.spi.InstanceProvider;
import com.feige.framework.api.spi.SpiLoader;
import com.feige.framework.api.spi.SpiNotFoundException;
import com.feige.framework.api.spi.InstancePostProcessor;
import com.feige.fim.utils.AssertUtil;
import com.feige.fim.utils.lg.Loggers;
import com.feige.fim.utils.ClassUtils;
import com.feige.fim.utils.ReflectionUtils;
import com.feige.fim.utils.StringUtils;
import com.feige.framework.utils.AppContext;
import com.feige.framework.utils.Configs;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public abstract class AbstractSpiLoader extends LifecycleAdapter implements SpiLoader {

    protected static final Logger LOG = Loggers.LOADER;
    protected final Map<Class<?>, List<Object>> instanceCache = new ConcurrentHashMap<>();
    protected final Map<String, Object> singletonObjectCache = new ConcurrentHashMap<>();
    protected final Map<String, Object> instanceProviderObjectCache = new ConcurrentHashMap<>();
    protected final Map<Class<?>, String> instanceNameCache = new ConcurrentHashMap<>();
    protected final List<InstancePostProcessor> processors = new ArrayList<>();
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);
    
    private final ApplicationContext applicationContext;

    public AbstractSpiLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize() throws IllegalStateException {
        if (isInitialized.compareAndSet(false, true)){
            List<Object> instanceList = this.doLoadInstance(InstancePostProcessor.class);
            for (Object instance : instanceList) {
                invokeAwareMethods(instance);
                this.processors.add((InstancePostProcessor)instance);
            }
            this.load(Configs.class);
            this.load(AppContext.class);
            this.load(InstanceProvider.class);
        }
    }
    
    @Override
    public void register(Class<?> clazz, List<Object> instances) {
        AssertUtil.notNull(clazz, "class");
        AssertUtil.notEmpty(instances, "instances");
        for (Object instance : instances) {
            String instanceName = getInstanceName(instance.getClass());
            this.singletonObjectCache.put(instanceName, instance);
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) throws SpiNotFoundException {
        AssertUtil.notBlank(key, "key");
        Object instance = this.singletonObjectCache.get(key);
        if (instance == null){
            AssertUtil.notNull(clazz, "class");
            List<Object> instanceList = loadClass(clazz);
            if (CollectionUtils.isEmpty(instanceList)) {
                throw new SpiNotFoundException(clazz);
            }
            instance = this.singletonObjectCache.get(key);
        }
        if (instance == null){
            throw new SpiNotFoundException(clazz, key);
        }
        instance = instanceProviderHandle(key, instance, clazz);
        return clazz.cast(instance);
    }

    @Override
    public <T> T getFirst(Class<T> clazz) throws SpiNotFoundException {
        AssertUtil.notNull(clazz, "class");
        List<Object> instanceList = loadClass(clazz);
        if (CollectionUtils.isEmpty(instanceList)){
            throw new SpiNotFoundException(clazz);
        }
        Object instance = instanceList.get(0);
        String instanceName = getInstanceName(instance.getClass());
        T t = null;
        try {
            t = get(instanceName, clazz);
        } catch (SpiNotFoundException ignored) {
            
        }
        if (t != null){
            return t;
        }
        instance = instanceProviderHandle(instanceName, instance, clazz);
        return clazz.cast(instance);
    }

    @Override
    public <T> List<T> getAll(Class<T> clazz) throws SpiNotFoundException {
        AssertUtil.notNull(clazz, "class");
        List<Object> instanceList = loadClass(clazz);
        if (instanceList == null){
            throw new SpiNotFoundException(clazz);
        }
        return instanceList.stream()
                .map(instance -> {
                    String instanceName = getInstanceName(instance.getClass());
                    T t = null;
                    try {
                        t = get(instanceName, clazz);
                    } catch (SpiNotFoundException ignored) {
                        
                    }
                    if (t != null){
                        return t;
                    }
                    return instance;
                })
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

   
    protected boolean checkInstance(Object instance) {
        return checkInstance(instance.getClass());
    }

    protected boolean checkInstance(Class<?> clazz) {
        boolean annotationPresent = clazz.isAnnotationPresent(SpiComp.class);
        if (!annotationPresent){
            LOG.warn("class = {}, No @SpiComp", clazz.getName());
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
        List<Object> instances;
        if (clazz.isInterface() || ClassUtils.isAbstractClass(clazz)){
            instances = doLoadInstance(clazz);
        }else {
            Object instance = createInstance(clazz);
            instances = Collections.singletonList(instance);
        }
        if (CollectionUtils.isNotEmpty(instances)){
            List<Object> newInstances = instances.stream()
                    .map(instance -> applyBeanPostProcessorsBeforeInitialization(instance, getInstanceName(instance.getClass())))
                    .collect(Collectors.toList());
            this.instanceCache.put(clazz, newInstances);
            if (InstanceProvider.class.equals(clazz)){
                newInstances.stream()
                        .collect(Collectors.groupingBy(instance -> ((InstanceProvider<?>) instance).getType()))
                        .forEach((k, v) -> {
                            v.sort(OrderComparator.getInstance());
                            this.instanceCache.put(k, v);
                        });
            }
            injectInstance(newInstances);
            invokeAwareMethods(newInstances);
            instances.forEach(instance -> applyBeanPostProcessorsAfterInitialization(instance, getInstanceName(instance.getClass())));
            register(clazz, newInstances);
        }else {
            LOG.warn("class = {}, No implementation classes have been registered", clazz.getName());
        }
    }
    
    protected Object createInstance(Class<?> clazz){
        if (checkInstance(clazz)){
            try {
                return ReflectionUtils.accessibleConstructor(clazz).newInstance();
            } catch (Exception e) {
                LOG.error("instance error:" , e);
                throw new RuntimeException(e);
            }
        }
        return null;
    }
    
    

    private Object instanceProviderHandle(String key, Object instance, Class<?> clazz) {
        if (instance instanceof InstanceProvider && !InstanceProvider.class.isAssignableFrom(clazz)) {
            InstanceProvider<?> instanceProvider = (InstanceProvider<?>) instance;
            if (!isSingleton(instance)){
                return instanceProvider.getInstance();
            }
            Object realInstance = instanceProviderObjectCache.get(key);
            if (realInstance == null){
                synchronized (instanceProviderObjectCache){
                    realInstance = instanceProviderObjectCache.get(key);
                    if (realInstance == null){
                        realInstance = instanceProvider.getInstance();
                        if (instanceProvider.isSingleton()){
                            instanceProviderObjectCache.put(key, realInstance);
                        }
                    }
                }
            }
            return realInstance;
        }
        return instance;
    }
    
    private boolean isSingleton(Object instance){
        if (instance instanceof InstanceProvider<?>){
            return ((InstanceProvider<?>) instance).isSingleton();
        }
        return true;
    }

    private Object applyBeanPostProcessorsBeforeInitialization(Object instance,  String instanceName) {
        for (InstancePostProcessor processor : processors) {
            instance = processor.postProcessBeforeInitialization(instance, instanceName);
        }
        return instance;
    }


    private void applyBeanPostProcessorsAfterInitialization(Object instance,  String instanceName) {
        for (InstancePostProcessor processor : processors) {
            processor.postProcessAfterInitialization(instance, instanceName);
        }
    }
    
    private String getInstanceName(Class<?> clazz){
        return this.instanceNameCache.computeIfAbsent(clazz, this::generateInstanceName);
    }
    
    private String generateInstanceName(Class<?> clazz){
        SpiComp spiCompAnnotation = clazz.getAnnotation(SpiComp.class);
        if (spiCompAnnotation != null){
            String value = spiCompAnnotation.value();
            if (StringUtils.isNotBlank(value)){
                return value;
            }
        }
        return StringUtils.uncapitalize(clazz.getSimpleName());
    }
    
    private void injectInstance(List<Object> instances){
        for (Object instance : instances) {
            injectInstance(instance);
        }
    }

    private void invokeAwareMethods(List<Object> instances){
        for (Object instance : instances) {
            invokeAwareMethods(instance);
        }
    }
    
    private void invokeAwareMethods(Object instance){
        if (instance instanceof SpiLoaderAware) {
            ((SpiLoaderAware) instance).setSpiLoader(this);
        }
        if (instance instanceof ApplicationContextAware){
            ((ApplicationContextAware) instance).setApplicationContext(applicationContext);
        }
        if (instance instanceof EnvironmentAware){
            ((EnvironmentAware) instance).setEnvironment(applicationContext.getEnvironment());
        }
    }

    private void injectInstance(Object instance){
        // 遍历类的所有字段，包括父类的字段
        ReflectionUtils.doWithFields(instance.getClass(), field -> {
            Class<?> type = field.getType();
            Object value = null;
            Inject inject = field.getAnnotation(Inject.class);
            if (inject != null) {
                String key = inject.value();
                if (StringUtils.isNotBlank(key)){
                    value = this.get(key, type);
                }else {
                    value = this.getFirst(type);
                }
            }

            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation != null){
                String configKey = valueAnnotation.value();
                value = applicationContext.getEnvironment().convert(type, configKey, null);
                // 空安全，空值不设置
                if (value == null && valueAnnotation.nullSafe()){
                    return;
                }
            }
            if (value != null){
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, instance, value);
            }
        }, field -> field.isAnnotationPresent(Inject.class) || field.isAnnotationPresent(Value.class));
    }
    
    protected abstract List<Object> doLoadInstance(Class<?> loadClass);
}
