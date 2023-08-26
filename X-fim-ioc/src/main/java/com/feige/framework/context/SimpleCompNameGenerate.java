package com.feige.framework.context;

import com.feige.framework.annotation.Comp;
import com.feige.framework.annotation.SpiComp;
import com.feige.framework.api.context.CompNameGenerate;
import com.feige.utils.common.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpiComp(interfaces = CompNameGenerate.class)
public class SimpleCompNameGenerate implements CompNameGenerate {

    protected final Map<Class<?>, String> compNameCache = new ConcurrentHashMap<>(64);
    @Override
    public String generateName(Class<?> cls) {
        return getInstanceName(cls);
    }


    private String getInstanceName(Class<?> cls){
        return this.compNameCache.computeIfAbsent(cls, this::generateInstanceName);
    }

    private String generateInstanceName(Class<?> cls){
        String name = getNameByComp(cls);
        if (StringUtils.isBlank(name)){
            name = getNameBySpiComp(cls);
        }
        if (StringUtils.isBlank(name)){
            name = StringUtils.uncapitalize(cls.getSimpleName());
        }
        return name;
    }
    
    
    private String getNameBySpiComp(Class<?> cls){
        SpiComp spiCompAnnotation = cls.getAnnotation(SpiComp.class);
        if (spiCompAnnotation != null){
            String value = spiCompAnnotation.value();
            if (StringUtils.isNotBlank(value)){
                return value;
            }
        }
        return null;
    }

    private String getNameByComp(Class<?> cls){
        Comp comp = cls.getAnnotation(Comp.class);
        if (comp != null){
            String value = comp.value();
            if (StringUtils.isNotBlank(value)){
                return value;
            }
        }
        return null;
    }
}
