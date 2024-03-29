package com.feige.framework.inject;

import com.feige.framework.annotation.CompName;
import com.feige.framework.annotation.DisableInject;
import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.context.api.LifecycleAdapter;
import com.feige.framework.inject.api.CompInjection;
import com.feige.framework.spi.api.NoSuchInstanceException;
import com.feige.utils.clazz.BeanUtils;
import com.feige.utils.clazz.ClassUtils;
import com.feige.utils.clazz.ReflectionUtils;
import com.feige.utils.common.StringUtils;
import com.feige.utils.javassist.AnnotationUtils;
import com.feige.utils.spi.annotation.SPI;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SPI(interfaces = CompInjection.class)
public class SimpleCompInjection extends LifecycleAdapter implements CompInjection {
    
    private ApplicationContext applicationContext;


    private Collection<?> injectCollection(Class<?>[] genericClasses, Class<?> propertyType){
        Collection<?> coll = null;
        Map<String, ?> map = applicationContext.getByType(genericClasses[0]);
        if (Set.class.isAssignableFrom(propertyType)){
            coll = new HashSet<>(map.values());
        }else if(List.class.isAssignableFrom(propertyType)){
            coll = new ArrayList<>(map.values());
        }
        if (coll == null){
            throw new IllegalArgumentException("Collection inject only support Set and List");
        }
        return coll;
    }

    private Map<String, ?> injectMap(Class<?>[] genericClasses){
        if (String.class.equals(genericClasses[0])) {
            throw new IllegalArgumentException("Map key must be String");
        }
        return applicationContext.getByType(genericClasses[1]);
    }

    private Object injectSingle(Method writeMethod, Class<?> propertyType){
        Object value;
        CompName compName = AnnotationUtils.getAnnotation(writeMethod, CompName.class);
        if (compName != null && StringUtils.isNotBlank(compName.value())){
            try {
                value = applicationContext.get(compName.value(), propertyType);
            }catch (NoSuchInstanceException e){
                if (compName.ifNullGetFirst()){
                    value = applicationContext.get(propertyType);
                }else {
                    throw e;
                }
            }
        }else {
            value = applicationContext.get(propertyType);
        }
        return value;
    }

    @Override
    public void inject(Object comp) {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(comp.getClass());
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod == null || AnnotationUtils.findAnnotation(writeMethod, DisableInject.class) != null) {
                continue;
            }
            Class<?>[] parameterTypes = writeMethod.getParameterTypes();
            Class<?> propertyType = parameterTypes[0];
            Class<?>[] genericClasses = ClassUtils.getGenericParameterTypes(writeMethod, 0);
            Object value;
            if (Collection.class.isAssignableFrom(propertyType)) {
                value = injectCollection(genericClasses, propertyType);
            } else if (Map.class.isAssignableFrom(propertyType)) {
                value = injectMap(genericClasses);
            } else {
               value = injectSingle(writeMethod, propertyType);
            }
            if (value == null){
                throw new NoSuchInstanceException(propertyType);
            }
            ReflectionUtils.makeAccessible(writeMethod);
            ReflectionUtils.invokeMethod(writeMethod, comp, value);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
