package com.feige.framework.module.api;


import com.feige.framework.context.api.ApplicationContext;

import java.net.URL;
import java.util.Set;

public interface ModuleContext extends ApplicationContext {
    
    
    
    URL[] getURLs();
    
    Set<String> getAssociatedModuleNames();
}
