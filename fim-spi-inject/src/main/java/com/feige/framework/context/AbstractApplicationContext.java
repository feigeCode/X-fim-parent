package com.feige.framework.context;

import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.context.api.ApplicationRunner;
import com.feige.framework.env.StandardEnvironment;
import com.feige.framework.env.api.ConfigFactory;
import com.feige.framework.env.api.Environment;
import com.feige.framework.factory.api.CompFactory;
import com.feige.framework.inject.api.CompInjection;
import com.feige.framework.module.api.ModuleContext;
import com.feige.framework.module.api.ModuleRepository;
import com.feige.framework.processor.api.CompPostProcessor;
import com.feige.framework.registry.CompRegistry;
import com.feige.framework.spi.DefaultSpiCompLoader;
import com.feige.framework.spi.api.NoSuchInstanceException;
import com.feige.framework.spi.api.SpiCompLoader;
import com.feige.framework.utils.AppContext;
import com.feige.framework.utils.Configs;
import com.feige.utils.logger.Loggers;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractApplicationContext implements ApplicationContext {

    protected final AtomicReference<AppState> appState = new AtomicReference<>(AppState.CREATED);

    protected ApplicationContext parent;

    private Thread shutdownHook;
    
    protected final Environment environment;

    protected final SpiCompLoader spiCompLoader;
    
    protected CompRegistry compRegistry;

    protected CompFactory compFactory;

    protected CompInjection compInjection;

    protected List<CompPostProcessor> processors;
    
    protected ModuleRepository moduleRepository;

    public AbstractApplicationContext(Environment environment, SpiCompLoader spiCompLoader) {
        this.environment = environment;
        this.spiCompLoader = spiCompLoader;
    }

    public AbstractApplicationContext() {
        this.spiCompLoader = createSpiLoader();
        this.environment = createEnvironment();
    }
   

    @Override
    public void initialize() throws IllegalStateException {
        if (appState.compareAndSet(AppState.CREATED, AppState.INITIALIZED)){
            doInit();
        }
        registerShutdownHook();
    }
    
    protected void doInit(){
        this.spiCompLoader.initialize();
        this.environment.setConfigFactory(this.spiCompLoader.loadSpiComp(ConfigFactory.class));
        this.environment.initialize();
        this.compRegistry = this.getSpiCompLoader().loadSpiComp(CompRegistry.class);
        this.compRegistry.initialize();
        this.compFactory = this.getSpiCompLoader().loadSpiComp(CompFactory.class);
        this.compFactory.initialize();
        this.compInjection = this.getSpiCompLoader().loadSpiComp(CompInjection.class);
        this.compInjection.initialize();
        this.moduleRepository = this.getSpiCompLoader().loadSpiComp(ModuleRepository.class);
        this.moduleRepository.initialize();
        this.processors = this.getSpiCompLoader().loadSpiComps(CompPostProcessor.class);
        for (CompPostProcessor processor : this.processors) {
            processor.initialize();
        }
        Configs.setEnvironment(environment);
        AppContext.setApplicationContext(this);
    }


    @Override
    public void start(String... args) throws IllegalStateException {
        try {
            initialize();
            doStart();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected void doStart(String... args) throws Exception {
        List<ApplicationRunner> applicationRunners = getByType(ApplicationRunner.class);
        for (ApplicationRunner applicationRunner : applicationRunners) {
            applicationRunner.run(this, args);
        }
    }
   
    @Override
    public void register(String compName, Object instance) {
        getCompRegistry().register(compName, instance);
    }

    @Override
    public Object getCompFromCache(String compName) {
        Object comp = getCompRegistry().getCompFromCache(compName);
        if (comp == null && getParent() != null){
            comp = getParent().getCompFromCache(compName);
        }
        return comp;
    }

    @Override
    public Object removeCompFromCache(String compName) {
        return getCompRegistry().removeCompFromCache(compName);
    }

    @Override
    public <T> T get(String compName, Class<T> requireType, Object... args) throws NoSuchInstanceException {
        T t = compFactory.get(compName, requireType);
        if (t == null && getParent() != null){
            t = getParent().get(compName, requireType, args);
        }
        return t;
    }

    @Override
    public <T> T get(Class<T> requireType, Object... args) throws NoSuchInstanceException {
        T t = compFactory.get(requireType);
        if (t == null && getParent() != null){
            t = getParent().get(requireType, args);
        }
        return t;
    }

    @Override
    public <T> List<T> getByType(Class<T> requireType) throws NoSuchInstanceException {
        List<T> ts = compFactory.getByType(requireType);
        if (CollectionUtils.isEmpty(ts) && getParent() != null){
            ts = getParent().getByType(requireType);
        }
        return ts;
    }

    public void registerShutdownHook() {
        if (this.shutdownHook == null) {
            this.shutdownHook = new Thread(this::destroy,this.moduleName() + "ShutdownHook");
            Runtime.getRuntime().addShutdownHook(this.shutdownHook);
        }
    }
    

    @Override
    public void destroy() throws IllegalStateException {
        if (appState.compareAndSet(AppState.INITIALIZED, AppState.DESTROY)){
            doDestroy();
        }
        
    }

    protected void doDestroy(){
        this.spiCompLoader.destroy();
        this.environment.destroy();
        this.compRegistry.destroy();
        this.compFactory.destroy();
        this.compInjection.destroy();
        this.moduleRepository.destroy();
        for (CompPostProcessor processor : this.processors) {
            processor.destroy();
        }
        if (this.shutdownHook != null) {
            try {
                Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
            } catch (IllegalStateException ignored) {

            }
        }
    }

    @Override
    public String moduleName() {
        return APP_NAME;
    }

    @Override
    public long getStartupDate() {
        return new Date().getTime();
    }
    

    @Override
    public ModuleRepository getModuleRepository() {
        return moduleRepository;
    }

    @Override
    public ModuleContext findModule(String moduleName) {
        return moduleRepository.findModule(moduleName);
    }

    @Override
    public List<ModuleContext> getModules() {
        return moduleRepository.getModules();
    }

    @Override
    public void addModule(ModuleContext module) {
        moduleRepository.addModule(module);
    }

    @Override
    public ModuleContext removeModule(String moduleName) {
        return moduleRepository.removeModule(moduleName);
    }

    protected Environment createEnvironment(){
        return new StandardEnvironment();
    }

    protected SpiCompLoader createSpiLoader(){
        SpiCompLoader spiCompLoader = new DefaultSpiCompLoader(this);
        Loggers.LOADER.info("使用" + DefaultSpiCompLoader.class.getName() + "加载器");
        return spiCompLoader;
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
    
    @Override
    public ApplicationContext getParent() {
        return parent;
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
    public CompInjection getCompInjection() {
        return this.compInjection;
    }

    @Override
    public List<CompPostProcessor> getPostProcessors() {
        return this.processors;
    }

}
