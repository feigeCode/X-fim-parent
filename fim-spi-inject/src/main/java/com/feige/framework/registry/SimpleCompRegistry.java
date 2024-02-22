package com.feige.framework.registry;

import com.feige.framework.context.api.LifecycleAdapter;
import com.feige.utils.common.AssertUtil;
import com.feige.utils.spi.annotation.SPI;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SPI(interfaces = CompRegistry.class)
public class SimpleCompRegistry extends LifecycleAdapter implements CompRegistry {

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


}
