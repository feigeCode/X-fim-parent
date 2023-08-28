package com.feige.framework.spi;


import com.feige.utils.spi.annotation.SpiComp;
import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.ApplicationContextAware;
import com.feige.framework.api.context.EnvironmentAware;
import com.feige.framework.api.context.LifecycleAdapter;
import com.feige.framework.api.context.SpiCompLoaderAware;
import com.feige.framework.api.spi.SpiCompLoader;
import com.feige.framework.api.spi.SpiCompProvider;
import com.feige.utils.clazz.ClassUtils;
import com.feige.utils.clazz.ReflectionUtils;
import com.feige.utils.common.AssertUtil;
import com.feige.utils.javassist.AnnotationUtils;
import com.feige.utils.logger.Loggers;
import com.feige.utils.order.OrderClassComparator;
import org.slf4j.Logger;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public abstract class AbstractSpiCompLoader extends LifecycleAdapter implements SpiCompLoader {

    protected static final Logger LOG = Loggers.LOADER;
    
    protected final Map<String, Class<?>> compNameAndImplClassCache = new ConcurrentHashMap<>(32);
    protected final Map<Class<?>, List<String>> spiTypeAndCompNamesCache = new ConcurrentHashMap<>(32);
    protected final Map<Class<?>, List<String>> providerTypeAndCompNamesCache = new ConcurrentHashMap<>(16);
    private final Map<Class<?>, Object> lockMap = new ConcurrentHashMap<>();
    protected final Set<String> ignoredClasses = new HashSet<>();
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);
    
    protected final ApplicationContext applicationContext;

    public AbstractSpiCompLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    

    @Override
    public void initialize() throws IllegalStateException {
        if (isInitialized.compareAndSet(false, true)){
            this.applicationContext.initialize();
            try {
                this.doGetImplClasses(SpiCompProvider.class);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
    }
    
    @Override
    public Class<?> get(String compName, Class<?> requireType) throws ClassNotFoundException {
        AssertUtil.notBlank(compName, "compName");
        AssertUtil.notNull(requireType, "requireType");
        return doGetImplClass(compName, requireType);
    }


    @Override
    public String get(Class<?> requireType) throws ClassNotFoundException {
        AssertUtil.notNull(requireType, "requireType");
        List<String> classes = doGetImplClasses(requireType);
        if (classes == null || classes.isEmpty()){
            return null;
        }
        return classes.get(0);
    }

    @Override
    public List<String> getByType(Class<?> requireType) throws ClassNotFoundException {
        AssertUtil.notNull(requireType, "requireType");
        return doGetImplClasses(requireType);
    }



    protected Class<?> doGetImplClass(String compName, Class<?> requireType) throws ClassNotFoundException {
        Class<?> cls = this.compNameAndImplClassCache.get(compName);
        if (cls == null){
            doGetImplClasses(requireType);
            cls = this.compNameAndImplClassCache.get(compName);
        }
        return cls;
    }
    
    
    private Object getLock(Class<?> requireType){
        return this.lockMap.computeIfAbsent(requireType, k -> new Object());
    }


    private void removeLock(Class<?> requireType){
        this.lockMap.remove(requireType);
    }

    protected <T> List<String> doGetImplClasses(Class<T> requireType) throws ClassNotFoundException {
        List<String> compNames = this.spiTypeAndCompNamesCache.get(requireType);
        if (compNames == null){
            synchronized (this.getLock(requireType)){
                compNames = this.spiTypeAndCompNamesCache.get(requireType);
                if (compNames == null){
                    compNames = new ArrayList<>();
                    List<Class<?>> classes = new ArrayList<>();
                    List<String> list = this.doLoadSpiImplClasses(requireType);
                    for (String className : list) {
                        if (this.ignoredClasses.contains(className)) {
                            continue;
                        }
                        Class<?> cls = ClassUtils.forName(className, this.applicationContext.getClassLoader());
                        if (isSpiComp(cls)){
                            LOG.warn(cls.getName() + " is not spiComp!");
                            continue;
                        }
                        classes.add(cls);
                    }
                    classes.sort(OrderClassComparator.getInstance());
                    for (Class<?> cls : classes) {
                        String compName = addImplClass(requireType, cls);
                        compNames.add(compName);
                    }
                }
            }
            this.removeLock(requireType);
        }
        List<String> providerNames = this.providerTypeAndCompNamesCache.getOrDefault(requireType, Collections.emptyList());
        compNames.addAll(providerNames);
        return compNames.stream()
                .sorted((c1, c2) -> {
                    Class<?> cls1 = this.compNameAndImplClassCache.get(c1);
                    Class<?> cls2 = this.compNameAndImplClassCache.get(c2);
                    return OrderClassComparator.getInstance().compare(cls1, cls2);
                })
                .collect(Collectors.toList());
    }
    
    
    protected <T> Class<T> convert(Class<?> cls){
        return (Class<T>) cls;
    }

    @Override
    public boolean isSpiComp(Class<?> cls) {
        if (cls == null) {
            return false;
        }
        if (cls.isInterface()) {
            return false;
        }
        if (Modifier.isAbstract(cls.getModifiers())) {
            return false;
        }
        return AnnotationUtils.findAnnotation(cls, SpiComp.class) != null;
    }

    private String addImplClass(Class<?> requireType, Class<?> compClass){
        String compName = applicationContext.getCompNameGenerate().generateName(compClass);
        this.compNameAndImplClassCache.put(compName, compClass);
        List<String> list = this.spiTypeAndCompNamesCache.computeIfAbsent(requireType, k -> new ArrayList<>());
        list.add(compName);
        if (SpiCompProvider.class.isAssignableFrom(compClass)){
            SpiComp spiComp = AnnotationUtils.findAnnotation(compClass, SpiComp.class);
            if (spiComp == null){
                throw new RuntimeException(compClass.getName() + " lack " + SpiComp.class.getName());
            }
            Class<?>[] classes = spiComp.provideTypes();
            if (classes.length == 1 && Object.class.getName().equals(classes[0].getName())){
                throw new RuntimeException(compClass.getName() + " lack provideTypes");
            }
            for (Class<?> providerType : classes) {
                List<String> compNames = this.providerTypeAndCompNamesCache.computeIfAbsent(providerType, k -> new ArrayList<>());
                compNames.add(compName);
            }
        }
        return compName;
    }
    
    protected abstract List<String> doLoadSpiImplClasses(Class<?> requireType);
    
    @Override
    public void addIgnoreImpl(String... implNames) {
        this.ignoredClasses.addAll(Arrays.asList(implNames));
    }


    @Override
    public <T> List<T> loadSpiComps(Class<T> requireType) {
        try {
            List<String> compNames = this.getByType(requireType);
            List<T> ts = new ArrayList<>();
            for (String compName : compNames) {
                Class<T> cls = this.convert(this.get(compName, requireType));
                T t = ReflectionUtils.accessibleConstructor(cls).newInstance();
                invokeAwareMethods(t);
                ts.add(t);
            }
            return ts;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T loadSpiComp(Class<T> requireType) {
        try {
            String compName = this.get(requireType);
            Class<T> cls = this.convert(this.get(compName, requireType));
            AssertUtil.notNull(cls, "cls");
            T t = ReflectionUtils.accessibleConstructor(cls).newInstance();
            invokeAwareMethods(t);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
