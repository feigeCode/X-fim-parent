package com.feige.framework.api.context;

@FunctionalInterface
public interface ApplicationRunner {
    
    void run(String... args) throws Exception;
}
