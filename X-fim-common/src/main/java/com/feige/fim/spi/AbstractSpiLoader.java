package com.feige.fim.spi;

import com.feige.api.annotation.Spi;
import com.feige.api.spi.SpiLoader;
import com.feige.api.spi.SpiNotFoundException;
import com.feige.fim.ioc.InjectAnnotationInstancePostProcessor;
import com.feige.fim.ioc.InstancePostProcessor;
import com.feige.fim.lg.Loggers;
import com.feige.fim.utils.ClassUtils;
import com.feige.fim.utils.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractSpiLoader implements SpiLoader {

    protected static final Logger LOG = Loggers.LOADER;
    protected final Map<Class<?>, List<Object>> instanceMap = new ConcurrentHashMap<>();
    protected final List<InstancePostProcessor> processors = new ArrayList<>();
    public static final Comparator<Object> SPI_ORDER = (o1, o2) -> {
        Spi spi1 = o1.getClass().getAnnotation(Spi.class);
        Spi spi2 = o2.getClass().getAnnotation(Spi.class);
        return Integer.compare(spi1.order(), spi2.order());
    };
    
    {
        processors.add(new InjectAnnotationInstancePostProcessor());
    }
    
    @Override
    public void register(Class<?> clazz, List<Object> instances) {
        this.instanceMap.put(clazz, instances);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) throws SpiNotFoundException {
        List<Object> instanceList = loadClass(clazz);
        if (instanceList != null && !instanceList.isEmpty()){
            for (Object spi : instanceList) {
                Spi spiAnn = spi.getClass().getAnnotation(Spi.class);
                String value;
                if (spiAnn != null){
                    value = spiAnn.value();
                }else {
                    value = spi.getClass().getName();
                }
                if (Objects.equals(value, key)){
                    return clazz.cast(spi);
                }
            }
            throw new SpiNotFoundException(clazz, key);
        }
        throw new SpiNotFoundException(clazz);
    }

    @Override
    public <T> T getFirst(Class<T> clazz) throws SpiNotFoundException {
        List<Object> instanceList = loadClass(clazz);
        if (CollectionUtils.isEmpty(instanceList)){
            throw new SpiNotFoundException(clazz);
        }
        return clazz.cast(instanceList.get(0));
    }

    @Override
    public <T> List<T> getAll(Class<T> clazz) throws SpiNotFoundException {
        List<Object> instanceList = loadClass(clazz);
        if (instanceList == null){
            return Collections.emptyList();
        }
        return instanceList.stream()
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

   
    protected boolean checkInstance(Object instance) {
        return checkInstance(instance.getClass());
    }

    protected boolean checkInstance(Class<?> clazz) {
        Spi spi = clazz.getAnnotation(Spi.class);
        if (spi == null){
            LOG.warn("class = {}, No @Spi", clazz.getName());
        }
        return spi != null;
    }

    protected  List<Object> loadClass(Class<?> clazz){
        List<Object> instanceList = instanceMap.get(clazz);
        if (instanceList == null || instanceList.isEmpty()){
            if (clazz.isInterface() || ClassUtils.isAbstractClass(clazz)){
                load(clazz);
            }else {
                Object instance = createInstance(clazz);
                register(clazz, Collections.singletonList(instance));
            }
            instanceList = instanceMap.get(clazz);
        }
        return instanceList;
    }
    
    protected Object createInstance(Class<?> clazz){
        if (checkInstance(clazz)){
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                LOG.error("instance error:" , e);
            }
        }
        throw new RuntimeException();
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
    
}
