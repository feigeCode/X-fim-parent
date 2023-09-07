package com.feige.framework.module;

import com.feige.framework.context.api.Lifecycle;
import com.feige.framework.module.api.ModuleContext;
import lombok.extern.slf4j.Slf4j;
import sun.misc.CompoundEnumeration;

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
    private final Map<String, Class<?>> classCache = new HashMap<>();
    private final ModuleContext moduleContext;

    public ModuleClassLoader(ModuleContext moduleContext,URL[] urLs) {
        super(urLs, moduleContext.getParent().getClassLoader());
        this.moduleContext = moduleContext;
        try {
            for (URL url : urLs) {
                if (url.getPath().endsWith(".jar")) {
                    String path = url.getPath();
                    jarFiles.add(new JarFile(path));
                }
            }
        }catch (Exception e){
            log.error("初始化类加载器错误：", e);
        }

    }

    
    public Class<?> getClassFromCache(String name) {
        return this.classCache.get(name);
    }


    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> cls = getClassFromCache(name);
        if (cls != null){
            return cls;
        }
        cls = findLoadedClass(name);
        if (cls != null){
            return cls;
        }
        try {
            return super.loadClass(name);
        }catch (Throwable t){
            for (String moduleName : moduleContext.getAssociatedModuleNames()) {
                ModuleContext associatedModuleContext = moduleContext.getParent().findModule(moduleName);
                cls = associatedModuleContext.getClassLoader().loadClass(name);
                if (cls != null){
                    return cls;
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
                    Class<?> cls = null;
                    try {
                        cls = loadClass(className);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalStateException(e);
                    }
                    if (cls != null) {
                        this.classCache.put(className, cls);
                    }
                }else {
                    log.warn("包名未通过校验，请检查==>className=" + className);
                }
            }
        }
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        Enumeration<URL>[] tmp = (Enumeration<URL>[]) new Enumeration<?>[1];
        tmp[0] = findResources(name);
        return new CompoundEnumeration<>(tmp);
    }

    @Override
    public void start(String... args) throws IllegalStateException {

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
