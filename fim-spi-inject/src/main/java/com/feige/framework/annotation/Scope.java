package com.feige.framework.annotation;

import com.feige.framework.spi.api.SpiScope;

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
public @interface Scope {

    SpiScope scope() default SpiScope.GLOBAL;

}
