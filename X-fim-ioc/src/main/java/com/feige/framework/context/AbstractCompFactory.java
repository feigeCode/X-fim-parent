package com.feige.framework.context;

import com.feige.framework.annotation.Comp;
import com.feige.framework.annotation.InitMethod;
import com.feige.framework.annotation.Prototype;
import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.ApplicationContextAware;
import com.feige.framework.api.context.CompFactory;
import com.feige.framework.api.context.CompNameGenerate;
import com.feige.framework.api.context.CompPostProcessor;
import com.feige.framework.api.context.EnvironmentAware;
import com.feige.framework.api.context.InitializingComp;
import com.feige.framework.api.context.LifecycleAdapter;
import com.feige.framework.api.context.SpiCompLoaderAware;
import com.feige.framework.api.spi.InstanceCreationException;
import com.feige.framework.api.spi.InstanceCurrentlyInCreationException;
import com.feige.framework.api.spi.NoSuchInstanceException;
import com.feige.framework.api.spi.ObjectFactory;
import com.feige.framework.api.spi.SpiCompLoader;
import com.feige.framework.api.spi.SpiCompProvider;
import com.feige.utils.clazz.ClassUtils;
import com.feige.utils.clazz.ReflectionUtils;
import com.feige.utils.common.AssertUtil;
import com.feige.utils.common.StringUtils;
import com.feige.utils.javassist.AnnotationUtils;
import com.feige.utils.logger.Loggers;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class AbstractCompFactory extends LifecycleAdapter implements CompFactory, ApplicationContextAware {

    protected static final Logger LOG = Loggers.LOADER;
    private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
    protected final Map<String, Object> singletonObjectCache = new ConcurrentHashMap<>(64);
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
    protected final Map<String, Object> instanceProviderObjectCache = new ConcurrentHashMap<>(32);
    protected final Map<Class<?>, List<String>> classInstancesNameCache = new ConcurrentHashMap<>(32);
    protected final List<CompPostProcessor> processors = new ArrayList<>();
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);
    
    protected boolean allowCircularReferences;

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected SpiCompLoader getSpiCompLoader(){
        return applicationContext.getSpiCompLoader();
    }

    protected CompNameGenerate getCompNameGenerate(){
        return applicationContext.getCompNameGenerate();
    }

    @Override
    public void initialize() throws IllegalStateException {
        if (isInitialized.compareAndSet(false, true)){
           
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
            this.classInstancesNameCache.computeIfAbsent(instance.getClass(), k -> new ArrayList<>()).add(instanceName);
            addSingleton(instanceName, instance);
        }
    }
    

    public boolean isSingletonCurrentlyInCreation(String instanceName) {
        return this.singletonsCurrentlyInCreation.contains(instanceName);
    }

    protected Object getSingleton(String instanceName) {
        Object singletonObject = this.singletonObjectCache.get(instanceName);
        if (singletonObject == null && isSingletonCurrentlyInCreation(instanceName)) {
            singletonObject = this.earlySingletonObjects.get(instanceName);
            if (singletonObject == null) {
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
    
    protected <T> T createInstance(String instanceName, Class<T> cls, Object... args){
        if (!isPrototype(cls) && isSingletonCurrentlyInCreation(instanceName)){
            throw new InstanceCurrentlyInCreationException(cls);
        }
        try {
            this.singletonsCurrentlyInCreation.add(instanceName);
            return doCreateInstance(instanceName, cls, args);
        }catch (Throwable e){
            LOG.error("create " + cls.getName() + " instance failure:", e);
            throw new InstanceCreationException(e, cls);
        }finally {
            this.singletonsCurrentlyInCreation.remove(instanceName);
        }
    }




    protected <T> T doCreateInstance(String instanceName, Class<T> type, Object... args) throws Exception {
        // 创建实例
        T instance = createInstance(type, args);
        // 设置配置属性值并放入三级缓存
        
        boolean earlySingletonExposure = (isSingleton(instance) && this.allowCircularReferences &&
                isSingletonCurrentlyInCreation(instanceName));
        if (earlySingletonExposure){
            addSingletonFactory(instanceName, () -> this.getEarlyInstanceReference(instanceName, instance));
        }
        // 为实例注入属性
        applicationContext.getCompInjection().inject(instance);
        // 初始化实例
        
        return initializeInstances(instanceName, instance) ;
    }

    protected  <T> T initializeInstances(String instanceName, Object instance) throws Exception {
        // 实现aware接口的调用对应的set方法
        invokeAwareMethods(instance);
        // 执行前置处理器
        instance = applyBeanPostProcessorsBeforeInitialization(instance, instanceName );
        // 执行初始化方法
        invokeInitMethods(instance);
        // 执行后置处理器
        instance = applyBeanPostProcessorsAfterInitialization(instance, instanceName);
        
        return (T) instance;
    }

    protected void addSingleton(String instanceName, Object singletonObject) {
        synchronized (this.singletonObjectCache) {
            this.singletonObjectCache.put(instanceName, singletonObject);
            this.singletonFactories.remove(instanceName);
            this.earlySingletonObjects.remove(instanceName);
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

    protected <T> T createInstance(Class<T> cls, Object... args){
        try {
            return applicationContext.getInstantiationStrategy().instantiate(cls, args);
        } catch (Exception e) {
            LOG.error("instance error:" , e);
            throw new RuntimeException(e);
        }
    }

    protected boolean isSingleton(Object instance){
        return !isPrototype(instance.getClass());
    }

    protected boolean isPrototype(Class<?> cls){
        return cls.isAnnotationPresent(Prototype.class);
    }

    private Object applyBeanPostProcessorsBeforeInitialization(Object instance,  String instanceName) {
        Object result = instance;
        for (CompPostProcessor processor : processors) {
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
        for (CompPostProcessor processor : processors) {
            Object currentInstance = processor.postProcessAfterInitialization(instance, instanceName);
            if (currentInstance == null) {
                return result;
            }
            result = currentInstance;
        }
        return instance;
    }

    private void invokeInitMethods(Object instance) throws Exception {
        if (instance instanceof InitializingComp){
            ((InitializingComp) instance).afterPropertiesSet();
        }

        ReflectionUtils.doWithMethods(instance.getClass(), (method) -> {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 0){
                ReflectionUtils.makeAccessible(method);
                ReflectionUtils.invokeMethod(method, instance);
            }else {
                Loggers.LOADER.warn("initMethod不支持传入参数！");
            }
        }, method -> method.isAnnotationPresent(InitMethod.class));
    }

    private void invokeAwareMethods(Object instance){
        if (instance instanceof SpiCompLoaderAware) {
            ((SpiCompLoaderAware) instance).setSpiCompLoader(applicationContext.getSpiCompLoader());
        }
        if (instance instanceof ApplicationContextAware){
            ((ApplicationContextAware) instance).setApplicationContext(applicationContext);
        }
        if (instance instanceof EnvironmentAware){
            ((EnvironmentAware) instance).setEnvironment(applicationContext.getEnvironment());
        }
    }
    
}
