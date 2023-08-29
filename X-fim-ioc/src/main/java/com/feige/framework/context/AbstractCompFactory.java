package com.feige.framework.context;

import com.feige.framework.annotation.InitMethod;
import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.ApplicationContextAware;
import com.feige.framework.api.context.CompFactory;
import com.feige.framework.api.context.CompNameGenerate;
import com.feige.framework.api.context.CompPostProcessor;
import com.feige.framework.api.context.CompRegistry;
import com.feige.framework.api.context.EnvironmentAware;
import com.feige.framework.api.context.InitializingComp;
import com.feige.framework.api.context.LifecycleAdapter;
import com.feige.framework.api.context.SpiCompLoaderAware;
import com.feige.framework.api.spi.InstanceCreationException;
import com.feige.framework.api.spi.InstanceCurrentlyInCreationException;
import com.feige.framework.api.spi.SpiCompLoader;
import com.feige.utils.clazz.ReflectionUtils;
import com.feige.utils.javassist.AnnotationUtils;
import com.feige.utils.logger.Loggers;
import com.feige.utils.spi.SpiScope;
import com.feige.utils.spi.annotation.SpiComp;
import org.slf4j.Logger;


import java.util.List;
import java.util.Objects;



public abstract class AbstractCompFactory extends LifecycleAdapter implements CompFactory, ApplicationContextAware {

    protected static final Logger LOG = Loggers.LOADER;
   
    
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
    
    protected List<CompPostProcessor> getProcessors(){
        return applicationContext.getPostProcessors();
    }
    
    protected CompRegistry getCompRegistry(){
        return applicationContext.getCompRegistry();
    }

    public boolean isGlobalCurrentlyInCreation(String instanceName) {
        return this.getCompRegistry().isGlobalCurrentlyInCreation(instanceName);
    }

    protected Object getCompFromCache(String instanceName) {
        return this.getCompRegistry().getCompFromCache(instanceName);
    }
    
    protected <T> T createInstance(String instanceName, Class<T> cls, Object... args){
        if (isGlobalCurrentlyInCreation(instanceName) || !this.getCompRegistry().addGlobalCurrentlyInCreation(instanceName)){
            throw new InstanceCurrentlyInCreationException(cls);
        }
        try {
            return doCreateInstance(instanceName, cls, args);
        }catch (Throwable e){
            LOG.error("create " + cls.getName() + " instance failure:", e);
            throw new InstanceCreationException(e, cls);
        }finally {
            this.getCompRegistry().removeGlobalCurrentlyInCreation(instanceName);
        }
    }

    protected <T> T doCreateInstance(String instanceName, Class<T> type, Object... args) throws Exception {
        // 创建实例
        T instance = createInstance(type, args);
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

  
    

    protected <T> T createInstance(Class<T> cls, Object... args){
        try {
            return applicationContext.getInstantiationStrategy().instantiate(cls, args);
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
        try {
            Class<?> cls = getSpiCompLoader().get(compName, type);
            SpiComp spiComp = AnnotationUtils.findAnnotation(cls, SpiComp.class);
            return Objects.equals(spiComp.scope(), scope);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isGlobal(Object instance){
        SpiComp spiComp = AnnotationUtils.findAnnotation(instance.getClass(), SpiComp.class);
        return Objects.equals(spiComp.scope(), SpiScope.GLOBAL);
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
    
}