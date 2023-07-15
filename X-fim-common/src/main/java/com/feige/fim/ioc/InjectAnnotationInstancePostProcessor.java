package com.feige.fim.ioc;

import com.feige.api.annotation.Inject;
import com.feige.api.annotation.Value;
import com.feige.fim.config.Configs;
import com.feige.fim.spi.SpiLoaderUtils;
import com.feige.fim.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class InjectAnnotationInstancePostProcessor implements InstancePostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object instance, String instanceName) throws Exception {
        return injectInstance(instance);
    }


    protected Object injectInstance(Object instance){
        List<Field> list = new ArrayList<>();
        // 遍历类的所有字段，包括父类的字段
        ReflectionUtils.doWithFields(instance.getClass(), field -> {
            if (field.isAnnotationPresent(Inject.class) || field.isAnnotationPresent(Value.class)) {
                list.add(field);
            }
        });

        for (Field field : list) {
            Class<?> type = field.getType();
            Object value = null;
            if (field.isAnnotationPresent(Inject.class)){
                value = SpiLoaderUtils.getFirst(type);
            }else if (field.isAnnotationPresent(Value.class)){
                Value valueAnnotation = field.getAnnotation(Value.class);
                String configKey = valueAnnotation.value();
                if (type == String.class){
                    value = Configs.getString(configKey);
                }else if (type == Integer.class || type == int.class){
                    value = Configs.getInt(configKey);
                }
            }
            if (value != null){
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, instance, value);
            }
        }

        return instance;
    }
}
