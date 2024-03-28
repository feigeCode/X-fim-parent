package com.feige.utils.clazz;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class BeanUtils {

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> cls){
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(cls);
            return beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

}
