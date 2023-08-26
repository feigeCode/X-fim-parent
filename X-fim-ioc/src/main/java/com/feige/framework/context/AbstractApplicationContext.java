package com.feige.framework.context;

import com.feige.framework.api.context.CompFactory;
import com.feige.framework.api.context.CompInjection;
import com.feige.framework.api.context.CompNameGenerate;
import com.feige.framework.api.context.CompPostProcessor;
import com.feige.framework.api.spi.InstantiationStrategy;
import com.feige.framework.spi.JdkSpiCompLoader;
import com.feige.framework.utils.AppContext;
import com.feige.framework.utils.Configs;
import com.feige.utils.logger.Loggers;
import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.Environment;
import com.feige.framework.api.context.LifecycleAdapter;
import com.feige.framework.api.spi.SpiCompLoader;
import com.feige.framework.api.spi.NoSuchInstanceException;
import com.feige.framework.spi.ConfigSpiCompLoader;

import java.util.List;
import java.util.Objects;

public abstract class AbstractApplicationContext extends LifecycleAdapter implements ApplicationContext {
    public static final String DEFAULT_LOADER_TYPE = ConfigSpiCompLoader.TYPE;
    private final Environment environment;
    
    private final SpiCompLoader spiCompLoader;
    
    private CompFactory compFactory;
    
    private InstantiationStrategy instantiationStrategy;
    
    private CompInjection compInjection;
    
    private CompNameGenerate compNameGenerate;
    
    private List<CompPostProcessor> processors;

    public AbstractApplicationContext(Environment environment, SpiCompLoader spiCompLoader) {
        this.environment = environment;
        this.spiCompLoader = spiCompLoader;
        initialize();
    }

    public AbstractApplicationContext(String type) {
        this.spiCompLoader = createSpiLoader(type);
        this.environment = createEnvironment();
        initialize();
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

    public AbstractApplicationContext() {
        this(DEFAULT_LOADER_TYPE);
    }

    private Environment createEnvironment(){
        return new StandardEnvironment();
    }
    private SpiCompLoader createSpiLoader(String type){
        SpiCompLoader spiCompLoader;
        if (Objects.equals(type, ConfigSpiCompLoader.TYPE)){
            spiCompLoader = new ConfigSpiCompLoader(this);
            Loggers.LOADER.info("使用" + ConfigSpiCompLoader.class.getName() + "加载器");
        }else {
            spiCompLoader = new JdkSpiCompLoader(this);
            Loggers.LOADER.info("使用" + JdkSpiCompLoader.class.getName() + "加载器");
        }
        return spiCompLoader;
    }

    @Override
    public void initialize() throws IllegalStateException {
        this.environment.initialize();
        this.compFactory = this.getSpiCompLoader().loadSpiComp(CompFactory.class);
        this.compNameGenerate = this.getSpiCompLoader().loadSpiComp(CompNameGenerate.class);
        this.instantiationStrategy = this.getSpiCompLoader().loadSpiComp(InstantiationStrategy.class);
        this.compInjection = this.getSpiCompLoader().loadSpiComp(CompInjection.class);
        this.processors = this.getSpiCompLoader().loadSpiComps(CompPostProcessor.class);
        this.get(Configs.class);
        this.get(AppContext.class);
    }

    @Override
    public CompFactory getCompFactory() {
        return this.compFactory;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public SpiCompLoader getSpiCompLoader() {
        return spiCompLoader;
    }

    @Override
    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    @Override
    public CompInjection getCompInjection() {
        return this.compInjection;
    }

    @Override
    public CompNameGenerate getCompNameGenerate() {
        return this.compNameGenerate;
    }

    @Override
    public List<CompPostProcessor> getPostProcessors() {
        return this.processors;
    }

    @Override
    public void register(String instanceName, Object instance) {
        this.compFactory.register(instanceName, instance);
    }

    @Override
    public <T> T get(String key, Class<T> requireType, Object... args) throws NoSuchInstanceException {
        return this.compFactory.get(key, requireType);
    }

    @Override
    public <T> T get(Class<T> requireType, Object... args) throws NoSuchInstanceException {
        return this.compFactory.get(requireType);
    }

    @Override
    public <T> List<T> getByType(Class<T> requireType) throws NoSuchInstanceException {
        return this.compFactory.getByType(requireType);
    }
}
