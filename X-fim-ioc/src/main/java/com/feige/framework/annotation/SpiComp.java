package com.feige.framework.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SpiComp {
    
    String value() default "";

    Class<?>[] interfaces() default Object.class;
}
