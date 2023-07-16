package com.feige.api.spi;

public interface InstancePostProcessor {

    /**
     * 对象实例化之后初始化之前执行（属性未注入）
     * @param instance 实例对象
     * @param instanceName 实例名称
     * @return
     */
    default Object postProcessBeforeInitialization(Object instance, String instanceName) {
        return instance;
    }


    /**
     * 初始化完成之后（属性已完成注入）
     * @param instance 实例对象
     * @param instanceName 实例名称
     */
    default void postProcessAfterInitialization(Object instance, String instanceName) {
        
    }
}
