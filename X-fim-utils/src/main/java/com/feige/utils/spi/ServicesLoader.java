package com.feige.utils.spi;

import com.feige.utils.clazz.ClassUtils;
import com.feige.utils.clazz.ReflectionUtils;
import com.feige.utils.common.AssertUtil;
import com.feige.utils.order.OrderComparator;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class ServicesLoader {
  public static final String SERVICES_PATH = "META-INF/services";

  static final Map<ClassLoader, Map<String, List<String>>> cache = new ConcurrentHashMap<>();
  
  static final List<String> ignoreServiceList = new ArrayList<>();
  
  public static void addIgnoreService(String... ignoreService){
    if (ignoreService != null && ignoreService.length > 0){
      ignoreServiceList.addAll(Arrays.asList(ignoreService));
    }
  }

  private ServicesLoader() {}


  public static  <T> List<T> loadServices(Class<T> spiType, ClassLoader classLoader) {
    AssertUtil.notNull(spiType, "'spiType' must not be null");
    ClassLoader classLoaderToUse = classLoader;
    if (classLoaderToUse == null) {
      classLoaderToUse = SpiConfigsLoader.class.getClassLoader();
    }
    List<String> factoryImplementationNames = loadServiceNames(spiType, classLoaderToUse);
    if (log.isTraceEnabled()) {
      log.trace("Loaded [" + spiType.getName() + "] names: " + factoryImplementationNames);
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
  
  public static List<String> loadServiceNames(Class<?> serviceType, ClassLoader classLoader){
    ClassLoader classLoaderToUse = classLoader;
    if (classLoaderToUse == null) {
      classLoaderToUse = SpiConfigsLoader.class.getClassLoader();
    }
    String serviceTypeName = serviceType.getName();
    return loadServices(serviceTypeName, classLoaderToUse).getOrDefault(serviceTypeName, Collections.emptyList());
  }


  private static Map<String, List<String>> loadServices(String serviceTypeName, ClassLoader classLoader) {
    Map<String, List<String>> result = cache.get(classLoader);
    if (result != null && result.get(serviceTypeName) != null) {
      return result;
    }

    String path = getPath(serviceTypeName);
    if (result == null){
      result = new HashMap<>();
    }
    try {
      Enumeration<URL> resources = classLoader.getResources(path);
      while (resources.hasMoreElements()) {
        URL url = resources.nextElement();
        Set<String> services = readServiceFile(url.openStream());
        result.computeIfAbsent(serviceTypeName, k -> new ArrayList<>()).addAll(services);
      }

      // Replace all lists with unmodifiable lists containing unique elements
      result.replaceAll((factoryType, implementations) -> implementations.stream().distinct()
              .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)));
      cache.put(classLoader, result);
    }
    catch (IOException ex) {
      throw new IllegalArgumentException("Unable to load services from location [" +
              path + "]", ex);
    }
    return result;
  }
  

  /**
   * Returns an absolute path to a service file given the class
   * name of the service.
   *
   * @param serviceName not {@code null}
   * @return SERVICES_PATH + serviceName
   */
  public static String getPath(String serviceName) {
    return SERVICES_PATH + "/" + serviceName;
  }

  /**
   * Reads the set of service classes from a service file.
   *
   * @param input not {@code null}. Closed after use.
   * @return a not {@code null Set} of service class names.
   * @throws IOException
   */
  
  public static Set<String> readServiceFile(InputStream input) throws IOException {
    HashSet<String> serviceClasses = new HashSet<>();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
      String line;
      while ((line = br.readLine()) != null) {
        int commentStart = line.indexOf('#');
        if (commentStart >= 0) {
          line = line.substring(0, commentStart);
        }
        line = line.trim();
        if (!line.isEmpty() && !ignoreServiceList.contains(line)) {
          serviceClasses.add(line);
        }
      }
      return serviceClasses;
    }
  }

  /**
   * Writes the set of service class names to a service file.
   *
   * @param output not {@code null}. Not closed after use.
   * @param services a not {@code null Collection} of service class names.
   * @throws IOException
   */
  public static void writeServiceFile(Collection<String> services, OutputStream output)
      throws IOException {
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
    for (String service : services) {
      writer.write(service);
      writer.newLine();
    }
    writer.flush();
  }
  
  @SuppressWarnings("unchecked")
  private static  <T> T createInstance(String serviceImplName, Class<T> factoryType, ClassLoader classLoader) {
    try {
      Class<?> factoryImplementationClass = ClassUtils.forName(serviceImplName, classLoader);
      if (!factoryType.isAssignableFrom(factoryImplementationClass)) {
        throw new IllegalArgumentException(
                "Class [" + serviceImplName + "] is not assignable to spi type [" + factoryType.getName() + "]");
      }
      return (T) ReflectionUtils.accessibleConstructor(factoryImplementationClass).newInstance();
    }
    catch (Throwable ex) {
      throw new IllegalArgumentException(
              "Unable to instantiate spi class [" + serviceImplName + "] for spi type [" + factoryType.getName() + "]",
              ex);
    }
  }
}
