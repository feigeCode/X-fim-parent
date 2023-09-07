package com.feige.framework.context.api;

@FunctionalInterface
public interface ApplicationRunner {
    
    void run(ApplicationContext applicationContext, String... args) throws Exception;
}
