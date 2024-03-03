package com.feige.framework.factory;

import com.feige.framework.annotation.InitMethod;
import com.feige.framework.annotation.Scope;
import com.feige.framework.aware.ApplicationContextAware;
import com.feige.framework.aware.EnvironmentAware;
import com.feige.framework.aware.SpiCompLoaderAware;
import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.context.api.InitializingComp;
import com.feige.framework.context.api.LifecycleAdapter;
import com.feige.framework.factory.api.CompFactory;
import com.feige.framework.processor.api.CompPostProcessor;
import com.feige.framework.registry.CompRegistry;
import com.feige.framework.spi.api.InstanceCreationException;
import com.feige.framework.spi.api.InstanceCurrentlyInCreationException;
import com.feige.framework.spi.api.SpiCompLoader;
import com.feige.framework.spi.api.SpiScope;
import com.feige.utils.clazz.ReflectionUtils;
import com.feige.utils.javassist.AnnotationUtils;
import com.feige.utils.logger.Loggers;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public abstract class AbstractCompFactory extends LifecycleAdapter implements CompFactory, ApplicationContextAware {

    protected static final Logger LOG = Loggers.LOADER;

    private final Set<String> globalCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected SpiCompLoader getSpiCompLoader(){
        return applicationContext.getSpiCompLoader();
    }


    protected List<CompPostProcessor> getProcessors(){
        return applicationContext.getPostProcessors();
    }
    
    protected CompRegistry getCompRegistry(){
        return applicationContext.getCompRegistry();
    }
    

    protected Object getCompFromCache(String instanceName) {
        return getCompRegistry().getCompFromCache(instanceName);
    }

    protected <T> T createOneOrModuleInstance(Class<T> requireType, String compName, Object... args) throws ClassNotFoundException {
        Class<?> implClass = getSpiCompLoader().get(requireType, compName);
        try {
            return doCreateInstance(requireType, compName, args);
        }catch (Throwable e){
            LOG.error("create " + implClass.getName() + " instance failure:", e);
            throw new InstanceCreationException(e, implClass);
        }
    }
    
    protected <T> T createInstance(Class<T> requireType, String compName, Object... args) throws ClassNotFoundException {
        Class<?> implClass = getSpiCompLoader().get(requireType, compName);
        if (isGlobalCurrentlyInCreation(compName) || !this.addGlobalCurrentlyInCreation(compName)){
            throw new InstanceCurrentlyInCreationException(requireType);
        }
        try {
            return doCreateInstance(requireType, compName, args);
        }catch (Throwable e){
            LOG.error("create " + implClass.getName() + " instance failure:", e);
            throw new InstanceCreationException(e, implClass);
        }finally {
            this.removeGlobalCurrentlyInCreation(compName);
        }
    }

    protected <T> T doCreateInstance(Class<T> requireType, String compName,  Object... args) throws Exception {
        // 创建实例
        T instance = instantiate(requireType, compName, args);
        // 为实例注入属性
        applicationContext.getCompInjection().inject(instance);
        // 初始化实例
        return initializeInstances(compName, instance) ;
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

  
    

    protected <T> T instantiate(Class<T> requireType, String compName, Object... args){
        try {
            return getSpiCompLoader().loadSpiComp(requireType, compName, args);
        } catch (Exception e) {
            LOG.error("instance error:" , e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isGlobal(Class<?> type, String compName) {
        return isEqual(type, compName, SpiScope.GLOBAL);
    }

    @Override
    public boolean isModule(Class<?> type, String compName) {
        return isEqual(type, compName, SpiScope.MODULE);
    }

    @Override
    public boolean isOne(Class<?> type, String compName) {
        return isEqual(type, compName, SpiScope.ONE);
    }
    
    
    private boolean isEqual(Class<?> type, String compName, SpiScope scope){
        Class<?> cls = null;
        try {
            cls = getSpiCompLoader().get(type, compName);
        } catch (ClassNotFoundException ignored) {
            
        }
        if (cls == null){
            ApplicationContext parent = applicationContext.getParent();
            if (parent != null){
                try {
                    cls = parent.getSpiCompLoader().get(type, compName);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (cls == null){
            throw new RuntimeException(type.getName() + " not found " + compName);
        }
        Scope scopeAnno = AnnotationUtils.findAnnotation(cls, Scope.class);
        SpiScope spiScope = SpiScope.GLOBAL;
        if (scopeAnno != null){
            spiScope = scopeAnno.scope();
        }
        return Objects.equals(spiScope, scope);
    }
    
    

    private Object applyBeanPostProcessorsBeforeInitialization(Object instance,  String instanceName) {
        Object result = instance;
        for (CompPostProcessor processor : getProcessors()) {
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
        for (CompPostProcessor processor : getProcessors()) {
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


    public boolean isGlobalCurrentlyInCreation(String compName) {
        return this.globalCurrentlyInCreation.contains(compName);
    }


    public boolean addGlobalCurrentlyInCreation(String compName) {
        return this.globalCurrentlyInCreation.add(compName);
    }

    public boolean removeGlobalCurrentlyInCreation(String compName) {
        return this.globalCurrentlyInCreation.remove(compName);
    }
    
}
