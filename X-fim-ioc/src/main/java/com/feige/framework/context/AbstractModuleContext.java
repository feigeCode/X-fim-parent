package com.feige.framework.context;

import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.Environment;
import com.feige.framework.api.context.ModuleContext;
import com.feige.framework.api.spi.SpiCompLoader;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractModuleContext extends AbstractApplicationContext implements ModuleContext {
    
    private final ApplicationContext parent;
    private final String moduleName;

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
    public void destroy() throws IllegalStateException {
        super.destroy();
        parent.removeModule(this.moduleName());
    }

    @Override
    public String moduleName() {
        return moduleName;
    }

    @Override
    public Environment getEnvironment() {
        return new StandardModuleEnvironment(parent.getEnvironment(), this);
    }

    @Override
    public ClassLoader getClassLoader() {
        return new ModuleClassLoader(this);
    }

    @Override
    public URL[] getURLs() {
        return new URL[0];
    }

    @Override
    public Set<String> getAssociatedModuleNames() {
        return new HashSet<>();
    }
}
