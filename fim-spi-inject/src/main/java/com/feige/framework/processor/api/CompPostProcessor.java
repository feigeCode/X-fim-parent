package com.feige.framework.processor.api;


import com.feige.framework.context.api.Lifecycle;

public interface CompPostProcessor extends Lifecycle {


    @Override
    default void initialize() throws IllegalStateException {

    }

    @Override
    default void start(String... args) throws IllegalStateException {

    }

    @Override
    default void destroy() throws IllegalStateException {

    }

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
