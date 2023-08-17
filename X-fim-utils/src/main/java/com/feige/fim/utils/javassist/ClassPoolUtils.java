package com.feige.fim.utils.javassist;


import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.LoaderClassPath;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author feige
 */
public class ClassPoolUtils {
    private static final Map<ClassLoader, ClassPool> CLASS_POOL_MAP = new ConcurrentHashMap<>();

    public static ClassPool getClassPool(ClassLoader classLoader, Class<?> clazz) {
        ClassPool pool = CLASS_POOL_MAP.get(classLoader);
        if (pool == null) {
            synchronized(CLASS_POOL_MAP) {
                pool = CLASS_POOL_MAP.get(classLoader);
                if (pool == null) {
                    pool = ClassPool.getDefault();
                    pool.insertClassPath(new ClassClassPath(clazz));
                    CLASS_POOL_MAP.put(classLoader, pool);
                }
            }
        }

        return pool;
    }

    public static ClassPool getClassPool(ClassLoader classLoader) {
        if (classLoader == null){
            return ClassPool.getDefault();
        }
        ClassPool pool = CLASS_POOL_MAP.get(classLoader);
        if (pool == null) {
            synchronized(CLASS_POOL_MAP) {
                pool = CLASS_POOL_MAP.get(classLoader);
                if (pool == null) {
                    pool = ClassPool.getDefault();
                    pool.insertClassPath(new LoaderClassPath(classLoader));
                    CLASS_POOL_MAP.put(classLoader, pool);
                }
            }
        }

        return pool;
    }

    public static void clear(ClassLoader classLoader) {
        synchronized(CLASS_POOL_MAP) {
            CLASS_POOL_MAP.remove(classLoader);
        }
    }
}
