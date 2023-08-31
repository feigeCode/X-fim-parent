package com.feige.framework.module;

import com.feige.framework.context.api.Lifecycle;
import com.feige.framework.module.api.ModuleContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

@Slf4j
public class ModuleClassLoader extends URLClassLoader implements Lifecycle {
    private static final Pattern VALID_CLASS_PATTERN = Pattern.compile("fim|service|dao|mapper|pojo|entity|po|dto|vo|listener|controller|common|api|utils|core|util|quartz|handle");
    private final List<JarFile> jarFiles = new ArrayList<>();
    private final Map<String, Class<?>> classCacheMap = new HashMap<>();
    private final ModuleContext moduleContext;

    public ModuleClassLoader(ModuleContext moduleContext) {
        super(moduleContext.getURLs(), moduleContext.getParent().getClassLoader());
        this.moduleContext = moduleContext;
        try {
            for (URL url : moduleContext.getURLs()) {
                if (url.getPath().endsWith(".jar")) {
                    jarFiles.add(new JarFile(url.getPath()));
                }
            }
        }catch (Exception e){
            log.error("初始化类加载器错误：", e);
        }

    }

    
    public Class<?> getClassFromCache(String name) {
        return this.classCacheMap.get(name);
    }


    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> clazz = getClassFromCache(name);
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
            for (String moduleName : moduleContext.getAssociatedModuleNames()) {
                ModuleContext associatedModuleContext = moduleContext.getParent().findModule(moduleName);
                clazz = associatedModuleContext.getClassLoader().loadClass(name);
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
    
}
