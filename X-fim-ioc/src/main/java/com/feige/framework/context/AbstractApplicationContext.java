package com.feige.framework.context;

import com.feige.fim.utils.lg.Loggers;
import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.Environment;
import com.feige.framework.api.context.LifecycleAdapter;
import com.feige.framework.api.spi.SpiLoader;
import com.feige.framework.api.spi.NoSuchInstanceException;
import com.feige.framework.extension.ConfigSpiLoader;
import com.feige.framework.extension.JdkSpiLoader;

import java.util.List;
import java.util.Objects;

public abstract class AbstractApplicationContext extends LifecycleAdapter implements ApplicationContext {
    public static final String DEFAULT_LOADER_TYPE = ConfigSpiLoader.TYPE;
    private final Environment environment;
    
    private final SpiLoader spiLoader;

    public AbstractApplicationContext(Environment environment, SpiLoader spiLoader) {
        this.environment = environment;
        this.spiLoader = spiLoader;
        initialize();
    }

    public AbstractApplicationContext(String type) {
        this.spiLoader = createSpiLoader(type);
        this.environment = createEnvironment();
        initialize();
    }

    public AbstractApplicationContext() {
        this(DEFAULT_LOADER_TYPE);
    }

    private Environment createEnvironment(){
        return new StandardEnvironment();
    }
    private SpiLoader createSpiLoader(String type){
        SpiLoader spiLoader;
        if (Objects.equals(type, ConfigSpiLoader.TYPE)){
            spiLoader = new ConfigSpiLoader(this);
            Loggers.LOADER.info("使用" + ConfigSpiLoader.class.getName() + "加载器");
        }else {
            spiLoader = new JdkSpiLoader(this);
            Loggers.LOADER.info("使用" + JdkSpiLoader.class.getName() + "加载器");
        }
        return spiLoader;
    }

    @Override
    public void initialize() throws IllegalStateException {
        this.environment.initialize();
        this.spiLoader.initialize();
    }


    @Override
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public SpiLoader getSpiLoader() {
        return spiLoader;
    }



    @Override
    public void register(String instanceName, Object instance) {
        this.spiLoader.register(instanceName, instance);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) throws NoSuchInstanceException {
        return this.spiLoader.get(key, clazz);
    }

    @Override
    public <T> T get(Class<T> clazz) throws NoSuchInstanceException {
        return this.spiLoader.get(clazz);
    }

    @Override
    public <T> List<T> getByType(Class<T> clazz) throws NoSuchInstanceException {
        return this.spiLoader.getByType(clazz);
    }
}
