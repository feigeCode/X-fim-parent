package com.feige.framework.extension;

import com.feige.framework.annotation.InitMethod;
import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.framework.annotation.Value;
import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.ApplicationContextAware;
import com.feige.framework.api.context.EnvironmentAware;
import com.feige.framework.api.context.LifecycleAdapter;
import com.feige.framework.api.context.SpiLoaderAware;
import com.feige.framework.api.spi.InstanceCreationException;
import com.feige.framework.api.spi.InstanceCurrentlyInCreationException;
import com.feige.framework.api.spi.ObjectFactory;
import com.feige.framework.api.spi.InstanceProvider;
import com.feige.framework.api.spi.SpiLoader;
import com.feige.framework.api.spi.NoSuchInstanceException;
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

public abstract class AbstractSpiLoader extends LifecycleAdapter implements SpiLoader {

    protected static final Logger LOG = Loggers.LOADER;
    private final Set<Class<?>> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
    protected final Map<String, Object> singletonObjectCache = new ConcurrentHashMap<>(64);
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
    protected final Map<String, Object> instanceProviderObjectCache = new ConcurrentHashMap<>(32);
    protected final Map<Class<?>, String> instanceNameCache = new ConcurrentHashMap<>(64);
    protected final Map<Class<?>, List<String>> classInstancesNameCache = new ConcurrentHashMap<>(32);
    protected final List<InstancePostProcessor> processors = new ArrayList<>();
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);
    
    private final ApplicationContext applicationContext;

    public AbstractSpiLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize() throws IllegalStateException {
        if (isInitialized.compareAndSet(false, true)){
            List<InstancePostProcessor> instanceList = this.doLoadSpiInstance(InstancePostProcessor.class);
            for (InstancePostProcessor instance : instanceList) {
                invokeAwareMethods(instance);
                this.processors.add(instance);
            }
            this.createInstances(Configs.class);
            this.createInstances(AppContext.class);
            this.createInstances(InstanceProvider.class);
        }
    }
    
    @Override
    public void register(String instanceName, Object instance) {
        AssertUtil.notNull(instanceName, "instanceName");
        AssertUtil.notNull(instance, "instance");
        synchronized (this.singletonObjectCache) {
            Object oldObject = this.singletonObjectCache.get(instanceName);
            if (oldObject != null) {
                throw new IllegalStateException("Could not register object [" + instance +
                        "] under instance name '" + instanceName + "': there is already object [" + oldObject + "] bound");
            }
            this.instanceNameCache.put(instance.getClass(), instanceName);
            this.classInstancesNameCache.computeIfAbsent(instance.getClass(), k -> new ArrayList<>()).add(instanceName);
            addSingleton(instanceName, instance);
        }
    }

    @Override
    public <T> T get(String instanceName, Class<T> requireType) throws NoSuchInstanceException {
        AssertUtil.notBlank(instanceName, "instanceName");
        AssertUtil.notNull(requireType, "requireType");
        return doGetInstance(instanceName, requireType);
    }
    

    @Override
    public <T> T get(Class<T> type) throws NoSuchInstanceException {
        AssertUtil.notNull(type, "type");
        return doGetInstance(null, type);
    }

    @Override
    public <T> List<T> getByType(Class<T> type) throws NoSuchInstanceException {
        AssertUtil.notNull(type, "type");
        return doGetInstances(type);
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
    
  
    private <T> T doGetInstance(String instanceName, Class<T> type){
        if (StringUtils.isBlank(instanceName)){
            instanceName = getFirstInstanceName(type);
        }
        
        Object singleton = null;
        if (StringUtils.isNotBlank(instanceName)){
            singleton = getSingleton(instanceName, type, true);
        }
        
        if (singleton == null) {
            Map<String, T> instanceMap = createInstances(type);
            instanceName = getFirstInstanceName(type);
            if (StringUtils.isNotBlank(instanceName)){
                singleton  = instanceMap.get(instanceName);
            }
        }
        
        if (singleton != null){
            return getObjectForInstance(instanceName, singleton, type);
        }
        throw StringUtils.isNotBlank(instanceName) ? new NoSuchInstanceException(type, instanceName) : new NoSuchInstanceException(type);
    }

    private String getFirstInstanceName(Class<?> type){
        List<String> instanceNames = classInstancesNameCache.get(type);
        if (CollectionUtils.isNotEmpty(instanceNames)){
            return instanceNames.get(0);
        }
        return null;
    }

    private <T> List<T> doGetInstances(Class<T> type){
        List<T> ts = new ArrayList<>();
        List<String> instanceNames = this.classInstancesNameCache.get(type);
        if (CollectionUtils.isNotEmpty(instanceNames)){
            for (String instanceName : instanceNames) {
                Object singleton = getSingleton(instanceName, type, true);
                if (singleton != null){
                    ts.add(getObjectForInstance(instanceName, singleton, type));
                }
            }
        }else {
            Map<String, T> instanceMap = createInstances(type);
            instanceMap.forEach((instanceName, t) -> ts.add(getObjectForInstance(instanceName, t, type)));
        }
        if (CollectionUtils.isEmpty(ts)){
            throw new NoSuchInstanceException(type);
        }
        return ts;
    }
    
    public boolean isSingletonCurrentlyInCreation(Class<?> type) {
        return this.singletonsCurrentlyInCreation.contains(type);
    }
    
    protected Object getSingleton(String instanceName, Class<?> type, boolean allowEarlyReference) {
        Object singletonObject = this.singletonObjectCache.get(instanceName);
        if (singletonObject == null && isSingletonCurrentlyInCreation(type)) {
            singletonObject = this.earlySingletonObjects.get(instanceName);
            if (singletonObject == null && allowEarlyReference) {
                synchronized (this.singletonObjectCache) {
                    singletonObject = this.singletonObjectCache.get(instanceName);
                    if (singletonObject == null) {
                        singletonObject = this.earlySingletonObjects.get(instanceName);
                        if (singletonObject == null) {
                            ObjectFactory<?> singletonFactory = this.singletonFactories.get(instanceName);
                            if (singletonFactory != null) {
                                singletonObject = singletonFactory.getObject();
                                this.earlySingletonObjects.put(instanceName, singletonObject);
                                this.singletonFactories.remove(instanceName);
                            }
                        }
                    }
                }
            }
        }
        return singletonObject;
    }

    public <T> T getEarlyInstanceReference(String instanceName, T instance){

        return instance;
    }
    
    private <T> Map<String, T> createInstances(Class<T> type){
        if (isSingletonCurrentlyInCreation(type)) {
            throw new InstanceCurrentlyInCreationException(type);
        }
        try {
            this.singletonsCurrentlyInCreation.add(type);
            return doCreateInstances(type);
        }catch (Throwable e){
            LOG.error("create " + type.getName() + " instance failure:", e);
            throw new InstanceCreationException(e, type);
        }finally {
            this.singletonsCurrentlyInCreation.remove(type);
        }
    }
    
    
    
    private <T> Map<String, T> doCreateInstances(Class<T> type){
        List<T> instances;
        // 创建实例
        if (type.isInterface() || ClassUtils.isAbstractClass(type)){
            instances = doLoadSpiInstance(type);
        }else {
            T instance = createInstance(type);
            if (instance == null){
                throw new RuntimeException();
            }
            instances = Collections.singletonList(instance);
        }
        if (CollectionUtils.isNotEmpty(instances)){
            // class -> instance names
            addClassInstanceName(type, instances);
            // 放入三级缓存
            for (T instance : instances) {
                String instanceName = getInstanceName(instance.getClass());
                addSingletonFactory(instanceName, () -> this.getEarlyInstanceReference(instanceName, instance));
            }
            // 为实例注入属性
            for (T instance : instances) {
                injectInstance(instance);
            }
            return initializeInstances(type, instances);
        }else {
            LOG.warn("class = {}, No implementation classes have been registered", type.getName());
        }
        return Collections.emptyMap();
    }
    
    private <T> Map<String, T> initializeInstances(Class<T> type, List<T> instances){
        Map<String, T> result = new HashMap<>();
        // 实现aware接口的调用对应的set方法
        for (T instance : instances) {
            invokeAwareMethods(instance);
        }
        // 执行前置处理器
        Map<String, T> wrappedInstanceMap = new HashMap<>();
        for (T instance : instances) {
            String instanceName = getInstanceName(instance.getClass());
            Object currentInstance = applyBeanPostProcessorsBeforeInitialization(instance, instanceName );
            wrappedInstanceMap.put(instanceName, (T) currentInstance);
        }
        // 执行初始化方法
        for (T wrappedInstance : wrappedInstanceMap.values()) {
            invokeInitMethods(wrappedInstance);
        }
        // 执行后置处理器
        wrappedInstanceMap.forEach((instanceName, instance) -> {
            Object currentInstance = applyBeanPostProcessorsAfterInitialization(instance, instanceName);
            T currentT = (T) currentInstance;
            addSingleton(instanceName, currentT);
            result.put(instanceName, currentT);
        });
        return result;
    }

    protected <T> void addClassInstanceName(Class<T> type, List<T> instances){
        for (Object newInstance : instances) {
            String instanceName = getInstanceName(newInstance.getClass());
            this.classInstancesNameCache.computeIfAbsent(type, k -> new ArrayList<>()).add(instanceName);
            if (newInstance instanceof InstanceProvider){
                Class<?> realType = ((InstanceProvider<?>) newInstance).getType();
                this.classInstancesNameCache.computeIfAbsent(realType, k -> new ArrayList<>()).add(instanceName);
                this.singletonsCurrentlyInCreation.add(realType);
            }
        }
    }
    
    protected void addSingleton(String instanceName, Object singletonObject) {
        synchronized (this.singletonObjectCache) {
            this.singletonObjectCache.put(instanceName, singletonObject);
            this.singletonFactories.remove(instanceName);
            this.earlySingletonObjects.remove(instanceName);
            if (singletonObject instanceof InstanceProvider){
                Class<?> realType = ((InstanceProvider<?>) singletonObject).getType();
                this.singletonsCurrentlyInCreation.remove(realType);
            }
        }
    }

    
    protected void addSingletonFactory(String instanceName, ObjectFactory<?> singletonFactory) {
        synchronized (this.singletonObjectCache) {
            if (!this.singletonObjectCache.containsKey(instanceName)) {
                this.singletonFactories.put(instanceName, singletonFactory);
                this.earlySingletonObjects.remove(instanceName);
            }
        }
    }
    
    protected <T> T createInstance(Class<T> clazz){
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
    
    

    private <T> T getObjectForInstance(String instanceName, Object instance, Class<T> requireType) {
        if (instance instanceof InstanceProvider && !InstanceProvider.class.isAssignableFrom(requireType)) {
            InstanceProvider<T> instanceProvider = (InstanceProvider<T>) instance;
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
            return (T) realInstance;
        }
        return (T) instance;
    }
    
    private boolean isSingleton(Object instance){
        if (instance instanceof InstanceProvider<?>){
            return ((InstanceProvider<?>) instance).isSingleton();
        }
        return true;
    }

    private Object applyBeanPostProcessorsBeforeInitialization(Object instance,  String instanceName) {
        Object result = instance;
        for (InstancePostProcessor processor : processors) {
            Object currentInstance = processor.postProcessBeforeInitialization(instance, instanceName);
            if (currentInstance == null) {
                return result;
            }
            result = currentInstance;
        }
        return instance;
    }


    private Object applyBeanPostProcessorsAfterInitialization(Object instance,  String instanceName) {
        Object result = instance;
        for (InstancePostProcessor processor : processors) {
            Object currentInstance = processor.postProcessAfterInitialization(instance, instanceName);
            if (currentInstance == null) {
                return result;
            }
            result = currentInstance;
        }
        return instance;
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
    
    private void invokeInitMethods(Object instance){
        ReflectionUtils.doWithMethods(instance.getClass(), (method) -> {
            if (method.isAnnotationPresent(InitMethod.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 0){
                    ReflectionUtils.makeAccessible(method);
                    ReflectionUtils.invokeMethod(method, instance);
                }else {
                    Loggers.LOADER.warn("initMethod不支持传入参数！");
                }
            }
        }, method -> method.isAnnotationPresent(InitMethod.class));
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
                String instanceName = inject.value();
                if (StringUtils.isNotBlank(instanceName)){
                    value = this.get(instanceName, type);
                }else {
                    value = this.get(type);
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
    
    protected abstract <T> List<T> doLoadSpiInstance(Class<T> loadClass);
}
