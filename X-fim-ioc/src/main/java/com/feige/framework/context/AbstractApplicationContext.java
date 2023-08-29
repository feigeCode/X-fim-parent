package com.feige.framework.context;

import com.feige.framework.api.config.ConfigFactory;
import com.feige.framework.api.context.CompFactory;
import com.feige.framework.api.context.CompInjection;
import com.feige.framework.api.context.CompNameGenerate;
import com.feige.framework.api.context.CompPostProcessor;
import com.feige.framework.api.context.CompRegistry;
import com.feige.framework.api.context.InstantiationStrategy;
import com.feige.framework.api.context.ModuleContext;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractApplicationContext extends LifecycleAdapter implements ApplicationContext {

    
    public static final String DEFAULT_LOADER_TYPE = ConfigSpiCompLoader.TYPE;

    
    protected final Map<String, ModuleContext> modelContextCache = new ConcurrentHashMap<>(16);

    
    private final AtomicReference<AppState> appState = new AtomicReference<>(AppState.CREATED);
    
    private final Environment environment;
    
    private final SpiCompLoader spiCompLoader;
    
    private CompRegistry compRegistry;
    
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


    public AbstractApplicationContext() {
        this(DEFAULT_LOADER_TYPE);
    }

    @Override
    public ModuleContext findModule(String moduleName) {
        return modelContextCache.get(moduleName);
    }

    @Override
    public List<ModuleContext> getModules() {
        return new ArrayList<>(modelContextCache.values());
    }

    @Override
    public void addModule(ModuleContext module) {
        module.initialize();
        modelContextCache.put(module.moduleName(), module);
    }

    @Override
    public ModuleContext removeModule(String moduleName) {
        ModuleContext moduleContext = modelContextCache.remove(moduleName);
        moduleContext.destroy();
        return moduleContext;
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
        if (appState.compareAndSet(AppState.CREATED, AppState.INITIALIZED)){
            this.spiCompLoader.initialize();
            this.environment.setConfigFactory(this.spiCompLoader.loadSpiComp(ConfigFactory.class));
            this.environment.initialize();
            this.compNameGenerate = this.getSpiCompLoader().loadSpiComp(CompNameGenerate.class);
            this.compNameGenerate.initialize();
            this.compRegistry = this.getSpiCompLoader().loadSpiComp(CompRegistry.class);
            this.compRegistry.initialize();
            this.compFactory = this.getSpiCompLoader().loadSpiComp(CompFactory.class);
            this.compFactory.initialize();
            this.instantiationStrategy = this.getSpiCompLoader().loadSpiComp(InstantiationStrategy.class);
            this.instantiationStrategy.initialize();
            this.compInjection = this.getSpiCompLoader().loadSpiComp(CompInjection.class);
            this.compInjection.initialize();
            this.processors = this.getSpiCompLoader().loadSpiComps(CompPostProcessor.class);
            for (CompPostProcessor processor : this.processors) {
                processor.initialize();
            }
            Configs.setEnvironment(environment);
            AppContext.setApplicationContext(this);
        }
    }

    @Override
    public void destroy() throws IllegalStateException {
        if (appState.compareAndSet(AppState.INITIALIZED, AppState.DESTROY)){
            Collection<ModuleContext> values = modelContextCache.values();
            for (ModuleContext moduleContext : values) {
                moduleContext.destroy();
            }
            this.spiCompLoader.destroy();
            this.environment.destroy();
            this.compNameGenerate.destroy();
            this.compRegistry.destroy();
            this.compFactory.destroy();
            this.instantiationStrategy.destroy();
            this.compInjection.destroy();
            for (CompPostProcessor processor : this.processors) {
                processor.destroy();
            }
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

    @Override
    public CompRegistry getCompRegistry() {
        return compRegistry;
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
    public void register(String compName, Object instance) {
        getCompRegistry().register(compName, instance);
    }

    @Override
    public Object getCompFromCache(String compName) {
        return getCompRegistry().getCompFromCache(compName);
    }

    @Override
    public Object removeCompFromCache(String compName) {
        return getCompRegistry().removeCompFromCache(compName);
    }

    @Override
    public <T> T get(String compName, Class<T> requireType, Object... args) throws NoSuchInstanceException {
        return compFactory.get(compName, requireType);
    }

    @Override
    public <T> T get(Class<T> requireType, Object... args) throws NoSuchInstanceException {
        return compFactory.get(requireType);
    }

    @Override
    public <T> List<T> getByType(Class<T> requireType) throws NoSuchInstanceException {
        return compFactory.getByType( requireType);
    }

    @Override
    public boolean isGlobal(Class<?> type, String compName) {
        return compFactory.isGlobal(type, compName);
    }

    @Override
    public boolean isModule(Class<?> type, String compName) {
        return compFactory.isModule(type, compName);
    }

    @Override
    public boolean isOne(Class<?> type, String compName) {
        return compFactory.isOne(type, compName);
    }
}
