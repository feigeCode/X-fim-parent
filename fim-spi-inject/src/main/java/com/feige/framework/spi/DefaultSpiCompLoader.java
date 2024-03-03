package com.feige.framework.spi;


import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.instantiate.SimpleInstantiateStrategy;
import com.feige.framework.instantiate.api.InstantiationStrategy;
import com.feige.utils.common.Pair;
import com.feige.utils.spi.ComponentsLoader;

import java.util.List;

public class DefaultSpiCompLoader extends AbstractSpiCompLoader {

    public DefaultSpiCompLoader(ApplicationContext applicationContext, InstantiationStrategy instantiationStrategy) {
        super(applicationContext, instantiationStrategy);
        this.instantiationStrategy = new SimpleInstantiateStrategy();
    }

    public DefaultSpiCompLoader(ApplicationContext applicationContext) {
        super(applicationContext, new SimpleInstantiateStrategy());
    }

    @Override
    protected List<Pair<String, String>> doLoadSpiImplClasses(Class<?> requireType) {
        return ComponentsLoader.loadServiceNames(requireType, this.applicationContext.getClassLoader());
    }
    
}
