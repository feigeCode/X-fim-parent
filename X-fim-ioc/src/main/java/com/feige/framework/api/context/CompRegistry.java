package com.feige.framework.api.context;

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


    default boolean isGlobalCurrentlyInCreation(String compName){
        return false;
    }
    
    default boolean addGlobalCurrentlyInCreation(String compName){
        return true;
    }

    default boolean removeGlobalCurrentlyInCreation(String compName){
        return true;
    }
    
}