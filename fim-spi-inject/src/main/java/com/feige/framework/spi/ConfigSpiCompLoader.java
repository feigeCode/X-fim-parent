package com.feige.framework.spi;



import com.feige.framework.context.api.ApplicationContext;
import com.feige.utils.spi.SpiConfigsLoader;

import java.util.List;



public class ConfigSpiCompLoader extends AbstractSpiCompLoader {

    public static final String TYPE = "config";

    public ConfigSpiCompLoader(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    protected List<String> doLoadSpiImplClasses(Class<?> requireType) {
        return SpiConfigsLoader.loadConfigNames(requireType, this.applicationContext.getClassLoader());
    }
    
}
