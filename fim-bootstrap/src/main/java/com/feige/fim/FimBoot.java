package com.feige.fim;


import com.feige.fim.constant.ServerConfigKey;
import com.feige.framework.context.StandardApplicationContext;
import com.feige.framework.env.api.Environment;

import java.util.Objects;

public class FimBoot {

    public static void main(String[] args) {
        StandardApplicationContext applicationContext = new StandardApplicationContext();
        Environment environment = applicationContext.getEnvironment();
        String runningMode = environment.getString(ServerConfigKey.RUNNING_MODE, "single");
        if (Objects.equals(runningMode, "single")){
            applicationContext.getSpiCompLoader().addIgnoreImpl("com.feige.cache.redis.RedisCacheManagerFactory");
        }
        applicationContext.start(args);
    }
    
    
    
    
}
