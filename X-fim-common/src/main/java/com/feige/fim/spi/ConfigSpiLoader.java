package com.feige.fim.spi;



import com.feige.fim.config.Configs;
import com.feige.fim.utils.StringUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ConfigSpiLoader extends AbstractSpiLoader {

    public static final String TYPE = "config";

    @Override
    public void load(Class<?> loadClass) {
        try {
            List<?> list = instanceMap.get(loadClass);
            if (list == null){
                String className = loadClass.getName();
                synchronized (className.intern()){
                    list = instanceMap.get(loadClass);
                    if (list == null){
                        List<Object> instanceList = new ArrayList<>();
                        List<String> classList = implClassList(className);
                        if (classList != null){
                            for (String clazz : classList) {
                                Class<?> implClass = Class.forName(clazz);
                                Object instance = implClass.newInstance();
                                if (this.checkInstance(instance)) {
                                    instanceList.add(applyBeanPostProcessorsBeforeInitialization(instance, getInstanceName(implClass)));
                                }
                            }
                            if (instanceList.size() > 1){
                                instanceList.sort(SPI_ORDER);
                            }
                        }
                        if (CollectionUtils.isNotEmpty(instanceList)){
                            register(loadClass, instanceList);
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
