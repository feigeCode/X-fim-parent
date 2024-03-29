package com.feige.utils.javassist;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class AnnotationUtils {


    public static <T extends Annotation> T getAnnotation(Class<?> cls, Class<T> annotation) {
        if (cls == null || annotation == null){
            return null;
        }
        return cls.getAnnotation(annotation);
    }
    public static <T extends Annotation> T findAnnotation(Class<?> cls, Class<T> annotation) {
        return getAnnotation(cls, annotation);
    }

    public static <T extends Annotation> T getAnnotation(Method method, Class<T> annotation) {
        if (method == null || annotation == null){
            return null;
        }
        return method.getAnnotation(annotation);
    }

    public static <T extends Annotation> T findAnnotation(Method method, Class<T> annotation) {
        T anno = getAnnotation(method, annotation);
        if (anno == null) {
            Class<?> cls = method.getDeclaringClass();
            anno = findAnnotationInInterfaces(cls, method, annotation);
            if (anno == null) {
                anno = findAnnotationInSuperClasses(cls, method, annotation);
            }
        }
        return anno;
    }

    private static <T extends Annotation> T findAnnotationInInterfaces(Class<?> cls, Method method, Class<T> annotation) {
        T anno = null;
        Class<?>[] interfaces = cls.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            try {
                Method m = anInterface.getDeclaredMethod(method.getName(), method.getParameterTypes());
                anno = getAnnotation(m, annotation);
                if (anno != null) {
                    break;
                }
                anno = findAnnotationInInterfaces(anInterface, method, annotation);
                if (anno != null) {
                    break;
                }
            } catch (NoSuchMethodException ignore) {
            }
        }
        return anno;
    }



    private static <T extends Annotation> T findAnnotationInSuperClasses(Class<?> cls, Method method, Class<T> annotation) {
        T anno = null;
        Class<?> superClass = cls.getSuperclass();
        if (superClass != null && !Object.class.equals(superClass)) {
            try {
                Method m = superClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                anno = getAnnotation(m, annotation);
                if (anno == null) {
                    anno = findAnnotationInInterfaces(superClass, method, annotation);
                    if (anno == null){
                        anno = findAnnotationInSuperClasses(superClass, method, annotation);
                    }
                }
            } catch (NoSuchMethodException ignore) {
            }
        }
        return anno;
    }

}
