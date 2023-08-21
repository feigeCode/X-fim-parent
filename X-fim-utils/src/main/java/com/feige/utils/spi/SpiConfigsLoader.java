package com.feige.utils.spi;

import com.feige.utils.common.AssertUtil;
import com.feige.utils.clazz.ClassUtils;
import com.feige.utils.clazz.ReflectionUtils;
import com.feige.utils.common.StringUtils;
import com.feige.utils.order.OrderComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SpiConfigsLoader {
    private static final Logger logger = LoggerFactory.getLogger(SpiConfigsLoader.class);
    public static final String SPI_CONFIGS = "META-INF/spi.configs";
    static final Map<ClassLoader, Map<String, List<String>>> cache = new ConcurrentHashMap<>();

    static final Set<String> ignoreServices = new HashSet<>();

    public static void addIgnoreService(String... ignoreService){
        if (ignoreService != null && ignoreService.length > 0){
            ignoreServices.addAll(Arrays.asList(ignoreService));
        }
    }
    
    private SpiConfigsLoader(){
        
    }
    
    public static  <T> List<T> loadConfigs(Class<T> spiType, ClassLoader classLoader) {
        AssertUtil.notNull(spiType, "'spiType' must not be null");
        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = SpiConfigsLoader.class.getClassLoader();
        }
        List<String> factoryImplementationNames = loadConfigNames(spiType, classLoaderToUse);
        if (logger.isTraceEnabled()) {
            logger.trace("Loaded [" + spiType.getName() + "] names: " + factoryImplementationNames);
        }
        List<T> result = new ArrayList<>(factoryImplementationNames.size());
        for (String factoryImplementationName : factoryImplementationNames) {
            result.add(createInstance(factoryImplementationName, spiType, classLoaderToUse));
        }
        if (result.size() > 1){
            result.sort(OrderComparator.getInstance());
        }
        return result;
    }

    public static List<String> loadConfigNames(Class<?> spiType, ClassLoader classLoader) {
        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = SpiConfigsLoader.class.getClassLoader();
        }
        String factoryTypeName = spiType.getName();
        return loadSpiConfigs(classLoaderToUse).getOrDefault(factoryTypeName, Collections.emptyList());
    }

    private static Map<String, List<String>> loadSpiConfigs(ClassLoader classLoader) {
        Map<String, List<String>> result = cache.get(classLoader);
        if (result != null) {
            return result;
        }

        result = new HashMap<>();
        try {
            Enumeration<URL> urls = classLoader.getResources(SPI_CONFIGS);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                Properties properties = new Properties();
                properties.load(url.openStream());
                for (Map.Entry<?, ?> entry : properties.entrySet()) {
                    String spiTypeName = ((String) entry.getKey()).trim();
                    List<String> spiImplNames =
                            StringUtils.commaSplitter.splitToList((String) entry.getValue());
                    for (String spiImplName : spiImplNames) {
                        if (ignoreServices.contains(spiImplName)){
                            continue;
                        }
                        result.computeIfAbsent(spiTypeName, key -> new ArrayList<>())
                                .add(spiImplName.trim());
                    }
                }
            }

            // Replace all lists with unmodifiable lists containing unique elements
            result.replaceAll((factoryType, implementations) -> implementations.stream().distinct()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)));
            cache.put(classLoader, result);
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Unable to load factories from location [" +
                    SPI_CONFIGS + "]", ex);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static  <T> T createInstance(String factoryImplementationName, Class<T> factoryType, ClassLoader classLoader) {
        try {
            Class<?> factoryImplementationClass = ClassUtils.forName(factoryImplementationName, classLoader);
            if (!factoryType.isAssignableFrom(factoryImplementationClass)) {
                throw new IllegalArgumentException(
                        "Class [" + factoryImplementationName + "] is not assignable to factory type [" + factoryType.getName() + "]");
            }
            return (T) ReflectionUtils.accessibleConstructor(factoryImplementationClass).newInstance();
        }
        catch (Throwable ex) {
            throw new IllegalArgumentException(
                    "Unable to instantiate factory class [" + factoryImplementationName + "] for factory type [" + factoryType.getName() + "]",
                    ex);
        }
    }
}
