package com.feige.utils.javassist;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class AnnotationUtils {


    public static <T extends Annotation> T findAnnotation(Class<?> cls, Class<T> annotation) {
        return cls.getAnnotation(annotation);
    }

    public static <T extends Annotation> T findAnnotation(Method method, Class<T> annotation) {
        return method.getAnnotation(annotation);
    }

    public static <T extends Annotation> T findAnnotation(Class<?> cls, Method method, Class<T> annotation) {
        T anno = findAnnotation(method, annotation);
        if (anno == null) {
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
                Method m = anInterface.getMethod(method.getName(), method.getParameterTypes());
                anno = findAnnotation(m, annotation);
                if (anno != null) {
                    break;
                }
            } catch (NoSuchMethodException ignore) {
            }
            anno = findAnnotationInInterfaces(anInterface, method, annotation);
            if (anno != null) {
                break;
            }
        }
        return anno;
    }



    private static <T extends Annotation> T findAnnotationInSuperClasses(Class<?> cls, Method method, Class<T> annotation) {
        T anno = null;
        Class<?> superClass = cls.getSuperclass();
        if (superClass != null) {
            try {
                Method m = superClass.getMethod(method.getName(), method.getParameterTypes());
                anno = findAnnotation(m, annotation);
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
