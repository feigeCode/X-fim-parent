package com.feige.utils.spi.annotation;

import com.feige.utils.spi.SpiScope;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Target({ElementType.TYPE})
public @interface SpiComp {
    
    String value() default "";
    
    SpiScope scope() default SpiScope.GLOBAL;

    Class<?>[] interfaces();
    
    Class<?>[] provideTypes() default Object.class;
}
