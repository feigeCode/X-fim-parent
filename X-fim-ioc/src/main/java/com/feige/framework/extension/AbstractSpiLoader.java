package com.feige.framework.extension;

import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.framework.annotation.Value;
import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.ApplicationContextAware;
import com.feige.framework.api.context.EnvironmentAware;
import com.feige.framework.api.context.LifecycleAdapter;
import com.feige.framework.api.context.SpiLoaderAware;
import com.feige.framework.api.spi.ObjectFactory;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public abstract class AbstractSpiLoader extends LifecycleAdapter implements SpiLoader {

    protected static final Logger LOG = Loggers.LOADER;
    private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
    protected final Map<Class<?>, List<Object>> earlyInstancesCache = new ConcurrentHashMap<>(16);
    protected final Map<String, Object> singletonObjectCache = new ConcurrentHashMap<>(64);
    protected final Map<String, Object> instanceProviderObjectCache = new ConcurrentHashMap<>(32);
    protected final Map<Class<?>, String> instanceNameCache = new ConcurrentHashMap<>(64);
    protected final Map<Class<?>, List<String>> classInstancesNameCache = new ConcurrentHashMap<>();
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
    public void register(String instanceName, Object instance) {
        AssertUtil.notNull(instanceName, "instanceName");
        AssertUtil.notNull(instance, "instance");
        this.instanceNameCache.putIfAbsent(instance.getClass(), instanceName);
        this.singletonObjectCache.put(instanceName, instance);
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
        instance = getObjectForInstance(key, instance, clazz);
        return clazz.cast(instance);
    }
    
    protected <T> T doGetInstance(String instanceName, Class<T> requireType){
        Object instanceObject = getSingleton(instanceName, true);
        if (instanceObject != null){
            if (LOG.isTraceEnabled()) {
                if (isSingletonCurrentlyInCreation(instanceName)) {
                    LOG.trace("Returning eagerly cached instance of singleton instance '" + instanceName +
                            "' that is not fully initialized yet - a consequence of a circular reference");
                }
                else {
                    LOG.trace("Returning cached instance of singleton instance '" + instanceName + "'");
                }
            }
            instanceObject = getObjectForInstance(instanceName, instanceObject, requireType);
        }else {
            
        }
        
        return (T) instanceObject;
    }
    
    
    protected Object getSingleton(String instanceName, boolean allowEarlyReference){
        Object instanceObject = this.singletonObjectCache.get(instanceName);
        if (instanceObject == null && isSingletonCurrentlyInCreation(instanceName)){
            instanceObject = this.earlySingletonObjects.get(instanceName);
            if (instanceObject == null && allowEarlyReference){
                synchronized (this.singletonObjectCache){
                    instanceObject = this.singletonObjectCache.get(instanceName);
                    if (instanceObject == null){
                        instanceObject = earlySingletonObjects.get(instanceName);
                        if (instanceObject == null){
                            ObjectFactory<?> singletonFactory = this.singletonFactories.get(instanceName);
                            if (singletonFactory != null) {
                                instanceObject = singletonFactory.getObject();
                                this.earlySingletonObjects.put(instanceName, instanceObject);
                                this.singletonFactories.remove(instanceName);
                            }
                        }
                    }
                }
            }
        }
        return instanceObject;
    }

    public boolean isSingletonCurrentlyInCreation(String instanceName) {
        return this.singletonsCurrentlyInCreation.contains(instanceName);
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
        instance = getObjectForInstance(instanceName, instance, clazz);
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
        if (clazz.isInterface() || ClassUtils.isAbstractClass(clazz)){
            List<String> list = classInstancesNameCache.get(clazz);
        }else {
            
        }
        List<Object> instanceList = earlyInstancesCache.get(clazz);
        if (instanceList == null){
            synchronized (earlyInstancesCache){
                instanceList = earlyInstancesCache.get(clazz);
                if(instanceList == null){
                    load(clazz);
                    instanceList = earlyInstancesCache.get(clazz);
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
            if (instance == null){
                return;
            }
            instances = Collections.singletonList(instance);
        }
        if (CollectionUtils.isNotEmpty(instances)){
            List<Object> newInstances = instances.stream()
                    .map(instance -> applyBeanPostProcessorsBeforeInitialization(instance, getInstanceName(instance.getClass())))
                    .collect(Collectors.toList());
            this.earlyInstancesCache.put(clazz, newInstances);
            Map<Class<?>, List<Object>> instanceProviderMap = null;
            if (InstanceProvider.class.equals(clazz)){
                instanceProviderMap = newInstances.stream()
                        .collect(Collectors.groupingBy(instance -> ((InstanceProvider<?>) instance).getType()));
                instanceProviderMap.forEach((k, v) -> {
                    v.sort(OrderComparator.getInstance());
                    earlyInstancesCache.put(k, v);
                });
            }
            injectInstance(newInstances);
            invokeAwareMethods(newInstances);
            for (Object instance : newInstances) {
                String instanceName = getInstanceName(instance.getClass());
                applyBeanPostProcessorsAfterInitialization(instance, instanceName);
                register(instanceName, instance);
                this.classInstancesNameCache.computeIfAbsent(clazz, k -> new ArrayList<>()).add(instanceName);
            }
            earlyInstancesCache.remove(clazz);
            if (instanceProviderMap != null){
                instanceProviderMap.forEach((k, v) -> earlyInstancesCache.remove(k));
            }
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
    
    

    private Object getObjectForInstance(String instanceName, Object instance, Class<?> clazz) {
        if (instance instanceof InstanceProvider && !InstanceProvider.class.isAssignableFrom(clazz)) {
            InstanceProvider<?> instanceProvider = (InstanceProvider<?>) instance;
            if (!isSingleton(instance)){
                return instanceProvider.getInstance();
            }
            Object realInstance = instanceProviderObjectCache.get(instanceName);
            if (realInstance == null){
                synchronized (instanceProviderObjectCache){
                    realInstance = instanceProviderObjectCache.get(instanceName);
                    if (realInstance == null){
                        realInstance = instanceProvider.getInstance();
                        if (instanceProvider.isSingleton()){
                            instanceProviderObjectCache.put(instanceName, realInstance);
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
