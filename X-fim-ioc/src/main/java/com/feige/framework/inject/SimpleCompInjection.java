package com.feige.framework.inject;

import com.feige.framework.annotation.Inject;
import com.feige.framework.context.api.LifecycleAdapter;
import com.feige.utils.spi.annotation.SPI;
import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.inject.api.CompInjection;
import com.feige.utils.clazz.ReflectionUtils;
import com.feige.utils.common.StringUtils;

@SPI(interfaces = CompInjection.class)
public class SimpleCompInjection extends LifecycleAdapter implements CompInjection {
    
    private ApplicationContext applicationContext;
    
    @Override
    public void inject(Object comp) {
        // 遍历类的所有字段，包括父类的字段
        ReflectionUtils.doWithFields(comp.getClass(), field -> {
            Class<?> type = field.getType();
            Object value = null;
            Inject inject = field.getAnnotation(Inject.class);
            if (inject != null) {
                String instanceName = inject.value();
                if (StringUtils.isNotBlank(instanceName)){
                    value = applicationContext.get(instanceName, type);
                }else {
                    value = applicationContext.get(type);
                }
            }

            if (value != null){
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, comp, value);
            }
        }, field -> field.isAnnotationPresent(Inject.class));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
