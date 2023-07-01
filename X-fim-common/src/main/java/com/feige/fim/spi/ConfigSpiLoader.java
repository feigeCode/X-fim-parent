package com.feige.fim.spi;


import com.feige.api.spi.Spi;
import com.feige.fim.config.Configs;
import com.feige.fim.utils.StringUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ConfigSpiLoader extends AbstractSpiLoader {

    public static final String TYPE = "config";

    @Override
    public void load(Class<?> loadClass) {
        try {
            if (!spiClass.isAssignableFrom(loadClass)){
                LOG.warn("Must implement {}.", spiClass.getName());
                return;
            }
            List<Spi> list = spiMap.get(loadClass);
            if (list == null){
                String className = loadClass.getName();
                synchronized (className.intern()){
                    list = spiMap.get(loadClass);
                    if (list == null){
                        List<Spi> spiList = new ArrayList<>();
                        List<String> classList = implClassList(className);
                        if (classList != null){
                            for (String clazz : classList) {
                                Class<?> implClass = Class.forName(clazz);
                                if (!loadClass.isAssignableFrom(implClass)){
                                    LOG.warn("Must implement {}.", spiClass.getName());
                                    continue;
                                }
                                Object instance = implClass.newInstance();
                                spiList.add((Spi) instance);
                            }
                            if (spiList.size() > 1){
                                spiList.sort(Comparator.comparing(Spi::order));
                            }
                        }
                        if (CollectionUtils.isNotEmpty(spiList)){
                            register(loadClass, spiList);
                        }else {
                            LOG.warn("class = {}, No implementation classes have been registered", className);
                        }
                    }
                }
            }
        }catch (Exception e){
            LOG.error("spi loader error:", e);
        }
    }

    private List<String> implClassList(String className){
        String implClass = Configs.getString(getSpiConfigKey(className));
        if (StringUtil.isNotBlank(implClass)){
            return Configs.commaSplitter.splitToList(implClass);
        }
        return null;
    }

    private String getSpiConfigKey(String className){
        return Configs.ConfigKey.SPI_LOADER_KEY + "." + className;
    }
}
