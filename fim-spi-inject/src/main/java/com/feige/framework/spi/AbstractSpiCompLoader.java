package com.feige.framework.spi;


import com.feige.framework.annotation.ConditionOnConfig;
import com.feige.framework.aware.ApplicationContextAware;
import com.feige.framework.aware.EnvironmentAware;
import com.feige.framework.aware.SpiCompLoaderAware;
import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.context.api.LifecycleAdapter;
import com.feige.framework.instantiate.api.InstantiationStrategy;
import com.feige.framework.spi.api.SpiCompLoader;
import com.feige.utils.clazz.ClassUtils;
import com.feige.utils.clazz.ReflectionUtils;
import com.feige.utils.common.AssertUtil;
import com.feige.utils.common.Pair;
import com.feige.utils.javassist.AnnotationUtils;
import com.feige.utils.logger.Loggers;
import com.feige.utils.order.OrderClassComparator;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractSpiCompLoader extends LifecycleAdapter implements SpiCompLoader {

    protected static final Logger LOG = Loggers.LOADER;
    
    protected final Map<String, Class<?>> compNameAndImplClassCache = new ConcurrentHashMap<>(32);
    protected final Map<Class<?>, List<String>> spiTypeAndCompNamesCache = new ConcurrentHashMap<>(32);
    protected final Set<String> ignoredClasses = new HashSet<>();

    protected final ApplicationContext applicationContext;

    protected InstantiationStrategy instantiationStrategy;

    public AbstractSpiCompLoader(ApplicationContext applicationContext, InstantiationStrategy instantiationStrategy) {
        this.applicationContext = applicationContext;
        this.instantiationStrategy = instantiationStrategy;
    }

    
    @Override
    public Class<?> get(Class<?> requireType, String compName) throws ClassNotFoundException {
        AssertUtil.notNull(requireType, "requireType");
        AssertUtil.notBlank(compName, "compName");
        Class<?> implClass = doGetImplClass(compName, requireType);
        if (implClass == null){
            implClass = this.getImplClassFromParent(compName, requireType);
        }
        return implClass;
    }


    @Override
    public String getCompNameFromCache(Class<?> implClass) {
        AssertUtil.notNull(implClass, "implClass");
        Set<Map.Entry<String, Class<?>>> entries = new HashMap<>(this.compNameAndImplClassCache).entrySet();
        for (Map.Entry<String, Class<?>> entry : entries) {
            if (implClass.equals(entry.getValue())){
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public List<String> getCompNamesByType(Class<?> requireType) throws ClassNotFoundException {
        AssertUtil.notNull(requireType, "requireType");
        List<String> compNames = doGetImplClasses(requireType);
        if (CollectionUtils.isEmpty(compNames)){
            compNames = getImplClassNamesFromParent(requireType);
        }
        return compNames;
    }

    protected Class<?> getImplClassFromParent(String compName, Class<?> requireType) throws ClassNotFoundException {
        ApplicationContext parent = applicationContext.getParent();
        if (parent != null){
            return parent.getSpiCompLoader().get(requireType, compName);
        }
        return null;
    }

    protected List<String> getImplClassNamesFromParent(Class<?> requireType) throws ClassNotFoundException {
        ApplicationContext parent = applicationContext.getParent();
        if (parent != null){
            return parent.getSpiCompLoader().getCompNamesByType( requireType);
        }
        return null;
    }

    protected Class<?> doGetImplClass(String compName, Class<?> requireType) throws ClassNotFoundException {
        Class<?> cls = this.getImplClassFormCache(compName);
        if (cls == null){
            doGetImplClasses(requireType);
            cls = this.getImplClassFormCache(compName);
        }
        return cls;
    }
    
    

    @Override
    public Class<?> getImplClassFormCache(String compName) {
        return this.compNameAndImplClassCache.get(compName);
    }
    
    protected List<String> getImplClassesFormCache(Class<?> requireType) {
        List<String> compNames = this.spiTypeAndCompNamesCache.get(requireType);
        if (compNames == null){
            return null;
        }
        return Collections.unmodifiableList(compNames);
    }

    protected boolean checkCondition(Class<?> cls){
        ConditionOnConfig condition = AnnotationUtils.findAnnotation(cls, ConditionOnConfig.class);
        if (condition == null){
            return true;
        }
        String key = condition.key();
        AssertUtil.notBlank(key, "ConditionOnConfig key");
        String value = condition.value();
        String configValue = applicationContext.getEnvironment().getString(key);
        return Objects.equals(value, configValue);
    }
    
    protected List<String> doLoadImplClasses(Class<?> requireType) throws ClassNotFoundException {
        List<Pair<String, String>> list = this.doLoadSpiImplClasses(requireType);
        for (Pair<String, String> compPair : list) {
            String compName = compPair.getK();
            String className = compPair.getV();
            if (this.ignoredClasses.contains(className)) {
                continue;
            }
            Class<?> cls = ClassUtils.forName(className, this.applicationContext.getClassLoader());
            if (!checkCondition(cls)){
                continue;
            }
            addImplClass(requireType, cls, compName);
        }
        List<String> compNames = spiTypeAndCompNamesCache.get(requireType);
        if (compNames == null) {
            return Collections.emptyList();
        }
        compNames.sort((c1, c2) -> {
                    Class<?> cls1 = this.compNameAndImplClassCache.get(c1);
                    Class<?> cls2 = this.compNameAndImplClassCache.get(c2);
                    return OrderClassComparator.getInstance().compare(cls1, cls2);
                });
        return Collections.unmodifiableList(compNames);
    }

    protected <T> List<String> doGetImplClasses(Class<T> requireType) throws ClassNotFoundException {
        List<String> compNames = this.getImplClassesFormCache(requireType);
        if (compNames == null){
            compNames = doLoadImplClasses(requireType);
        }
        return compNames;
    }
    
    
    protected <T> Class<T> convert(Class<?> cls){
        return (Class<T>) cls;
    }

    private void addImplClass(Class<?> requireType, Class<?> compClass, String compName){
        this.compNameAndImplClassCache.put(compName, compClass);
        List<String> list = this.spiTypeAndCompNamesCache.computeIfAbsent(requireType, k -> new ArrayList<>());
        list.add(compName);
    }
    
    protected abstract List<Pair<String, String>> doLoadSpiImplClasses(Class<?> requireType);
    
    @Override
    public void addIgnoreImpl(String... implNames) {
        this.ignoredClasses.addAll(Arrays.asList(implNames));
    }


    @Override
    public <T> List<T> loadSpiComps(Class<T> requireType) {
        try {
            List<String> compNames = this.getCompNamesByType(requireType);
            List<T> ts = new ArrayList<>();
            for (String compName : compNames) {
                Class<T> cls = this.convert(this.get(requireType, compName));
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
    public <T> T loadSpiComp(Class<T> requireType, Object... args) {
        try {
            List<String> compNames = this.getCompNamesByType(requireType);
            return loadSpiComp(requireType, compNames.get(0), args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T loadSpiComp(Class<T> requireType, String compName, Object... args) {
        try {
            Class<T> cls = this.convert(this.get(requireType, compName));
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

    @Override
    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }
}
