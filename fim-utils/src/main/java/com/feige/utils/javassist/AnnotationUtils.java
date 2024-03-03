package com.feige.utils.javassist;


import java.lang.annotation.Annotation;

public abstract class AnnotationUtils {


    public static <T extends Annotation> T findAnnotation(Class<?> cls, Class<T> annotation) {
        return cls.getAnnotation(annotation);
    }

}
