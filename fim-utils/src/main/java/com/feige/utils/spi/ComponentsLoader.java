package com.feige.utils.spi;

import com.feige.utils.clazz.ClassUtils;
import com.feige.utils.clazz.ReflectionUtils;
import com.feige.utils.common.AssertUtil;
import com.feige.utils.common.Pair;
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
public class ComponentsLoader {
  public static final String SERVICES_PATH = "META-INF/components";

  static final Map<ClassLoader, Map<String, List<String>>> cache = new ConcurrentHashMap<>();

  private ComponentsLoader() {}


  public static  <T> List<Pair<String, T>> loadComponents(Class<T> spiType, ClassLoader classLoader) {

    AssertUtil.notNull(spiType, "'spiType' must not be null");
    ClassLoader classLoaderToUse = classLoader;
    if (classLoaderToUse == null) {
      classLoaderToUse = ComponentsLoader.class.getClassLoader();
    }
    List<Pair<String, String>> factoryImplementationNames = loadComponentNames(spiType, classLoaderToUse);
    if (log.isTraceEnabled()) {
      log.trace("Loaded [" + spiType.getName() + "] names: " + factoryImplementationNames);
    }
    List<Pair<String, T>> result = new ArrayList<>(factoryImplementationNames.size());
    for (Pair<String, String> pair : factoryImplementationNames) {
      String factoryImplementationName = pair.getV();
      String name = pair.getK();
      T instance = createInstance(factoryImplementationName, spiType, classLoaderToUse);
      result.add(Pair.of(name, instance));
    }
    if (result.size() > 1){
      result.sort(OrderComparator.getInstance());
    }
    return result;
  }

  public static List<Pair<String, String>> loadComponentNames(Class<?> serviceType, ClassLoader classLoader){
    ClassLoader classLoaderToUse = classLoader;
    if (classLoaderToUse == null) {
      classLoaderToUse = ComponentsLoader.class.getClassLoader();
    }
    String serviceTypeName = serviceType.getName();
    List<String> names = loadComponents(serviceTypeName, classLoaderToUse).getOrDefault(serviceTypeName, Collections.emptyList());
    ArrayList<Pair<String, String>> result = new ArrayList<>();
    for (String name : names) {
      String[] comps = name.split("=");
      if (comps.length != 2){
        throw new IllegalArgumentException(name + " component  illegal");
      }
      result.add(Pair.of(comps[0], comps[1]));
    }
    return result;
  }


  private static Map<String, List<String>> loadComponents(String serviceTypeName, ClassLoader classLoader) {
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
        Set<String> services = readComponentFile(url.openStream());
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

  public static Set<String> readComponentFile(InputStream input) throws IOException {
    HashSet<String> serviceClasses = new HashSet<>();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
      String line;
      while ((line = br.readLine()) != null) {
        int commentStart = line.indexOf('#');
        if (commentStart >= 0) {
          line = line.substring(0, commentStart);
        }
        line = line.trim();
        if (!line.isEmpty()) {
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
  public static void writeComponentFile(Collection<String> services, OutputStream output)
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
