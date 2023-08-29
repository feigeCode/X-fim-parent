package com.feige.framework.context;

import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.ApplicationContextAware;
import com.feige.framework.api.context.Lifecycle;
import com.feige.framework.api.context.ModuleContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

@Slf4j
public class ModuleClassLoader extends URLClassLoader implements Lifecycle, ApplicationContextAware {
    private static final Pattern VALID_CLASS_PATTERN = Pattern.compile("service|dao|mapper|pojo|entity|po|dto|vo|listener|controller|common|api|utils|core|util|quartz");
    private final List<JarFile> jarFiles = new ArrayList<>();
    private final Map<String, Class<?>> classCacheMap = new HashMap<>();
    private final Set<String> associatedModuleNames = new HashSet<>();
    private final String moduleName;
    private ApplicationContext applicationContext;

    public ModuleClassLoader(String moduleName, URL[] urls, ClassLoader parent) {
        super(urls, parent);
        this.moduleName = moduleName;
        try {
            for (URL url : urls) {
                if (url.getPath().endsWith(".jar")) {
                    jarFiles.add(new JarFile(url.getPath()));
                }
            }
        }catch (Exception e){
            log.error("初始化类加载器错误：", e);
        }

    }

    
    public Class<?> getClassFromCache(String name) throws ClassNotFoundException {
        if (this.classCacheMap.containsKey(name)) {
            return this.classCacheMap.get(name);
        }
        throw new ClassNotFoundException(name);
    }


    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> clazz = classCacheMap.get(name);
        if (clazz != null){
            return clazz;
        }
        clazz = findLoadedClass(name);
        if (clazz != null){
            return clazz;
        }
        try {
            return super.loadClass(name);
        }catch (Throwable t){
            for (String moduleName : associatedModuleNames) {
                ModuleContext moduleContext = applicationContext.findModule(moduleName);
                clazz = moduleContext.getClassLoader().loadClass(name);
                if (clazz != null){
                    return clazz;
                }
            }
        }
        throw new ClassNotFoundException(name);
    }

    @Override
    public void initialize() throws IllegalStateException {
        for (JarFile jarFile : this.jarFiles) {
            Enumeration<JarEntry> en = jarFile.entries();
            while (en.hasMoreElements()) {
                JarEntry je = en.nextElement();
                String name = je.getName();
                if (!name.endsWith(".class")) {
                    continue;
                }
                String className = name.replace(".class", "").replaceAll("/", ".");
                if (VALID_CLASS_PATTERN.matcher(className).find()) {
                    Class<?> clazz = null;
                    try {
                        clazz = loadClass(className);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalStateException(e);
                    }
                    if (clazz != null) {
                        this.classCacheMap.put(className, clazz);
                    }
                }else {
                    log.warn("包名未通过校验，请检查==>className=" + className);
                }
            }
        }
    }

    @Override
    public void start() throws IllegalStateException {

    }

    @Override
    public void destroy() throws  IllegalStateException {
        try {
            this.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getModuleName() {
        return this.moduleName;
    }
    

    
    public Set<String> getAssociatedModuleNames() {
        return associatedModuleNames;
    }

    public void addAssociatedModuleNames(Set<String> associatedModuleNames){
        this.associatedModuleNames.addAll(associatedModuleNames);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
