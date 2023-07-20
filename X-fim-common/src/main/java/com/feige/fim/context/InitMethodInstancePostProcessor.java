package com.feige.fim.context;

import com.feige.api.annotation.InitMethod;
import com.feige.api.spi.InstancePostProcessor;
import com.feige.fim.lg.Loggers;
import com.feige.fim.utils.ReflectionUtils;

public class InitMethodInstancePostProcessor implements InstancePostProcessor {
    
    @Override
    public void postProcessAfterInitialization(Object instance, String instanceName) {
        ReflectionUtils.doWithLocalMethods(instance.getClass(), (method) -> {
            if (method.isAnnotationPresent(InitMethod.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 0){
                    ReflectionUtils.makeAccessible(method);
                    ReflectionUtils.invokeMethod(method, instance);
                }else {
                    Loggers.LOADER.warn("initMethod不支持传入参数！");
                }
            }
        });
    }
}
