package com.feige.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.METHOD})
@Inherited
@Documented
public @interface CompName {
    /**
     * config key
     * @return
     */
    String value();

    /**
     * if null get first
     * @return if null get first
     */
    boolean ifNullGetFirst() default false;
}
