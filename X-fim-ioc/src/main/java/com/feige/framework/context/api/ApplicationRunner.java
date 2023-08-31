package com.feige.framework.context.api;

@FunctionalInterface
public interface ApplicationRunner {
    
    void run(String... args) throws Exception;
}
