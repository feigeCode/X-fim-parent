package com.feige.framework.context;

import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.api.context.ModuleContext;

public abstract class AbstractModuleContext extends AbstractApplicationContext implements ModuleContext {
    
    private ApplicationContext parent;
    
    @Override
    public ApplicationContext getParent() {
        return parent;
    }


    @Override
    public void destroy() throws IllegalStateException {
        super.destroy();
        parent.removeModule(this.moduleName());
    }
}
