package com.feige.framework.module.api;


import com.feige.framework.context.api.ApplicationContext;

import java.net.URL;
import java.util.Set;

public interface ModuleContext extends ApplicationContext {
    
    ApplicationContext getParent();
    
    
    String moduleName();
    
    
    URL[] getURLs();
    
    Set<String> getAssociatedModuleNames();
}
