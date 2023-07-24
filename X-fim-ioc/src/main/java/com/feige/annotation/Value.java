package com.feige.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.METHOD})
@Inherited
@Documented
public @interface Value {
    /**
     * config key
     * @return
     */
    String value();

    /**
     * null safe ，null not set field
     * @return is null safe
     */
    boolean nullSafe() default true;
}
