package com.feige.framework.api.context;


import com.feige.framework.annotation.SPI;

@SPI
public interface CompPostProcessor {

    /**
     * invokeAwareMethods之后invokeInitMethods之前
     * @param instance 实例对象
     * @param instanceName 实例名称
     * @return
     */
    default Object postProcessBeforeInitialization(Object instance, String instanceName) {
        return instance;
    }


    /**
     * invokeInitMethods之后
     * @param instance 实例对象
     * @param instanceName 实例名称
     * @return 
     */
    default Object postProcessAfterInitialization(Object instance, String instanceName) {
        return instance;
    }
}
