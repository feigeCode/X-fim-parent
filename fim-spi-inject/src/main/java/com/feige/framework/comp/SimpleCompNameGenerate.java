package com.feige.framework.comp;

import com.feige.framework.context.api.LifecycleAdapter;
import com.feige.utils.spi.annotation.SPI;
import com.feige.framework.comp.api.CompNameGenerate;
import com.feige.utils.common.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SPI(interfaces = CompNameGenerate.class)
public class SimpleCompNameGenerate extends LifecycleAdapter implements CompNameGenerate {

    protected final Map<Class<?>, String> compNameCache = new ConcurrentHashMap<>(64);
    @Override
    public String generateName(Class<?> cls) {
        return getInstanceName(cls);
    }


    private String getInstanceName(Class<?> cls){
        return this.compNameCache.computeIfAbsent(cls, this::generateInstanceName);
    }

    private String generateInstanceName(Class<?> cls){
        String name = getNameBySpiComp(cls);
        if (StringUtils.isBlank(name)){
            name = StringUtils.uncapitalize(cls.getSimpleName());
        }
        return name;
    }
    
    
    private String getNameBySpiComp(Class<?> cls){
        SPI SPIAnnotation = cls.getAnnotation(SPI.class);
        if (SPIAnnotation != null){
            String value = SPIAnnotation.value();
            if (StringUtils.isNotBlank(value)){
                return value;
            }
        }
        return null;
    }
    
}
