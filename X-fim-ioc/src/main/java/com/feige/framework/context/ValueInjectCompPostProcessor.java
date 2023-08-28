package com.feige.framework.context;

import com.feige.utils.spi.annotation.SpiComp;
import com.feige.framework.annotation.Value;
import com.feige.framework.api.context.CompPostProcessor;
import com.feige.framework.api.context.Environment;
import com.feige.framework.api.context.EnvironmentAware;
import com.feige.utils.clazz.ReflectionUtils;

@SpiComp(interfaces = CompPostProcessor.class)
public class ValueInjectCompPostProcessor implements CompPostProcessor, EnvironmentAware {

    private Environment environment;

    @Override
    public Object postProcessBeforeInitialization(Object instance, String instanceName) {
        setConfigValue(instance);
        return instance;
    }


    private void setConfigValue(Object instance){
        // 遍历类的所有字段，包括父类的字段
        ReflectionUtils.doWithFields(instance.getClass(), field -> {
            Class<?> type = field.getType();
            Object value = null;
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation != null){
                String configKey = valueAnnotation.value();
                value = environment.convert(type, configKey, null);
                // 空安全，空值不设置
                if (value == null && valueAnnotation.nullSafe()){
                    return;
                }
            }
            if (value != null){
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, instance, value);
            }
        }, field -> field.isAnnotationPresent(Value.class));
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
