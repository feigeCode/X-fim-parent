package com.feige.framework.processor;

import com.feige.framework.context.api.LifecycleAdapter;
import com.feige.framework.processor.api.CompPostProcessor;
import com.feige.utils.spi.annotation.SPI;
import com.feige.framework.annotation.Value;
import com.feige.framework.env.api.Environment;
import com.feige.framework.aware.EnvironmentAware;
import com.feige.utils.clazz.ReflectionUtils;

@SPI(interfaces = CompPostProcessor.class)
public class ValueInjectCompPostProcessor extends LifecycleAdapter implements CompPostProcessor, EnvironmentAware {

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
