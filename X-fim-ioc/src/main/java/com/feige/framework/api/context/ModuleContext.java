package com.feige.framework.api.context;


import java.net.URL;
import java.util.Set;

public interface ModuleContext extends ApplicationContext {
    
    ApplicationContext getParent();
    
    
    String moduleName();
    
    
    URL[] getURLs();
    
    Set<String> getAssociatedModuleNames();
}
