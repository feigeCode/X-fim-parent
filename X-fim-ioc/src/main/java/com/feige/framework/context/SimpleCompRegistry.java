package com.feige.framework.context;

import com.feige.framework.api.context.CompRegistry;
import com.feige.framework.api.context.LifecycleAdapter;
import com.feige.utils.common.AssertUtil;
import com.feige.utils.spi.annotation.SpiComp;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SpiComp(interfaces = CompRegistry.class)
public class SimpleCompRegistry extends LifecycleAdapter implements CompRegistry {

    private final Set<String> globalCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
    protected final Map<String, Object> globalObjectCache = new ConcurrentHashMap<>(64);

    @Override
    public void register(String compName, Object instance) {
        AssertUtil.notNull(compName, "compName");
        AssertUtil.notNull(instance, "instance");
        synchronized (this.globalObjectCache) {
            Object oldObject = this.globalObjectCache.get(compName);
            if (oldObject != null) {
                throw new IllegalStateException("Could not register object [" + instance +
                        "] under component name '" + compName + "': there is already object [" + oldObject + "] bound");
            }
            this.globalObjectCache.put(compName, instance);
            removeGlobalCurrentlyInCreation(compName);
        }
    }
    
    @Override
    public Object getCompFromCache(String compName) {
        return this.globalObjectCache.get(compName);
    }

    @Override
    public Object removeCompFromCache(String compName) {
        return this.globalObjectCache.remove(compName);
    }

    @Override
    public boolean isGlobalCurrentlyInCreation(String compName) {
        return this.globalCurrentlyInCreation.contains(compName);
    }


    @Override
    public boolean addGlobalCurrentlyInCreation(String compName) {
        return this.globalCurrentlyInCreation.add(compName);
    }

    @Override
    public boolean removeGlobalCurrentlyInCreation(String compName) {
        return this.globalCurrentlyInCreation.remove(compName);
    }
}
