package com.feige.framework.context;

import com.feige.annotation.InitMethod;
import com.feige.annotation.SpiComp;
import com.feige.api.spi.InstancePostProcessor;
import com.feige.fim.utils.lg.Loggers;
import com.feige.fim.utils.ReflectionUtils;

@SpiComp
public class InitMethodInstancePostProcessor implements InstancePostProcessor {
    
    @Override
    public void postProcessAfterInitialization(Object instance, String instanceName) {
        ReflectionUtils.doWithMethods(instance.getClass(), (method) -> {
            if (method.isAnnotationPresent(InitMethod.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 0){
                    ReflectionUtils.makeAccessible(method);
                    ReflectionUtils.invokeMethod(method, instance);
                }else {
                    Loggers.LOADER.warn("initMethod不支持传入参数！");
                }
            }
        }, method -> method.isAnnotationPresent(InitMethod.class));
    }
}
