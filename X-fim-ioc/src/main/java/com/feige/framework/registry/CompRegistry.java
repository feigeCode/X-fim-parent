package com.feige.framework.registry;

import com.feige.framework.context.api.Lifecycle;

public interface CompRegistry extends Lifecycle {

    /**
     * register object
     * @param compName instance name
     * @param instance object 
     */
    void register(String compName, Object instance);

    /**
     * get  object
     * @param compName
     * @return
     */
    Object getCompFromCache(String compName);

    /**
     * remove object
     * @param compName compName
     * @return
     */
    Object removeCompFromCache(String compName);
    
}
