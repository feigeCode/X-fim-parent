package com.feige.framework.context;

import com.feige.framework.api.config.ConfigFactory;
import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.CompFactory;
import com.feige.framework.api.context.CompInjection;
import com.feige.framework.api.context.CompNameGenerate;
import com.feige.framework.api.context.CompPostProcessor;
import com.feige.framework.api.context.CompRegistry;
import com.feige.framework.api.context.Environment;
import com.feige.framework.api.context.InstantiationStrategy;
import com.feige.framework.api.context.ModuleBootstrap;
import com.feige.framework.api.context.ModuleContext;
import com.feige.framework.api.spi.SpiCompLoader;
import com.feige.framework.utils.Configs;
import com.feige.utils.common.AssertUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractModuleContext extends AbstractApplicationContext implements ModuleContext {
    
    private final ApplicationContext parent;
    private final String moduleName;
    private ModuleClassLoader moduleClassLoader;

    public AbstractModuleContext(String type, ApplicationContext parent, String moduleName) {
        super(type);
        this.parent = parent;
        this.moduleName = moduleName;
    }

    public AbstractModuleContext(ApplicationContext parent, String moduleName) {
        this.parent = parent;
        this.moduleName = moduleName;
    }

    @Override
    public ApplicationContext getParent() {
        return parent;
    }

    @Override
    public void initialize() throws IllegalStateException {
        this.moduleClassLoader = new ModuleClassLoader(this);
        this.moduleClassLoader.initialize();
        this.spiCompLoader.initialize();
        this.environment.setConfigFactory(getParent().getSpiCompLoader().loadSpiComp(ConfigFactory.class));
        this.environment.initialize();
        this.compNameGenerate = this.getSpiCompLoader().loadSpiComp(CompNameGenerate.class);
        this.compRegistry = this.getSpiCompLoader().loadSpiComp(CompRegistry.class);
        this.compFactory = this.getSpiCompLoader().loadSpiComp(CompFactory.class);
        this.instantiationStrategy = this.getSpiCompLoader().loadSpiComp(InstantiationStrategy.class);
        this.compInjection = this.getSpiCompLoader().loadSpiComp(CompInjection.class);
        this.processors = this.getSpiCompLoader().loadSpiComps(CompPostProcessor.class);
        ModuleBootstrap moduleBootstrap = this.getSpiCompLoader().loadSpiComp(ModuleBootstrap.class);
        moduleBootstrap.initialize();
        moduleBootstrap.run(this);
    }

    @Override
    protected Environment createEnvironment() {
        return new StandardModuleEnvironment(this);
    }

    @Override
    protected SpiCompLoader createSpiLoader(String type) {
        return super.createSpiLoader(type);
    }

    @Override
    public void destroy() throws IllegalStateException {
        super.destroy();
        parent.removeModule(this.moduleName());
    }

    @Override
    public String moduleName() {
        return moduleName;
    }
    

    @Override
    public ClassLoader getClassLoader() {
        return moduleClassLoader;
    }

    @Override
    public URL[] getURLs() {
        try {
            String absolutePath = new File("").getAbsolutePath();
            String modulesDir = getParent().getEnvironment().getString(Configs.ConfigKey.MODULES_DIR_KEY, Configs.DEFAULT_MODULES_DIR);
            File file;
            if (Configs.DEFAULT_MODULES_DIR.equals(modulesDir)){
                file = new File(absolutePath + File.separator + modulesDir + moduleName());
            }else {
                file = new File(modulesDir + moduleName());
            }
            File[] jarFiles = file.listFiles((f) -> f.isFile() && f.getName().endsWith(".jar"));
            AssertUtil.notNull(jarFiles, "jarFiles");
            AssertUtil.check(jarFiles.length > 0, "jarFiles.length = 0");
            URL[] urls = new URL[jarFiles.length];
            for (int i = 0; i < jarFiles.length; i++) {
                urls[i] = jarFiles[i].toURI().toURL();
            }
            return urls;
        } catch (MalformedURLException e) {
           throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getAssociatedModuleNames() {
        return new HashSet<>();
    }
}
