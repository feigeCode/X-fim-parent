package com.feige.framework.spi;


import com.feige.framework.api.context.ApplicationContext;
import com.feige.utils.spi.ServicesLoader;

import java.util.List;

public class JdkSpiCompLoader extends AbstractSpiCompLoader {
    
    public static final String TYPE = "jdk";

    public JdkSpiCompLoader(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    protected List<String> doLoadSpiImplClasses(Class<?> requireType) {
        return ServicesLoader.loadServiceNames(requireType, this.applicationContext.getClassLoader());
    }
    
}
