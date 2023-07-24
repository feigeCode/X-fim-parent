package com.feige.framework.boot;



import com.feige.framework.order.OrderComparator;
import com.feige.framework.config.Configs;
import com.feige.fim.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ConfigSpiLoader extends AbstractSpiLoader {

    public static final String TYPE = "config";

    @Override
    public List<Object>  doLoadInstance(Class<?> loadClass) {
        List<Object> instanceList = new ArrayList<>();
        try {
            String className = loadClass.getName();
            List<String> classList = implClassList(className);
            if (classList != null){
                for (String clazz : classList) {
                    Class<?> implClass = Class.forName(clazz);
                    Object instance = createInstance(implClass);
                    if (instance == null){
                        continue;
                    }
                    instanceList.add(instance);
                }
                if (instanceList.size() > 1){
                    instanceList.sort(OrderComparator.getInstance());
                }
            }
        }catch (Exception e){
            LOG.error("spi loader error:", e);
        }
        return instanceList;
    }

    private List<String> implClassList(String className){
        String implClass = Configs.getString(getSpiConfigKey(className));
        if (StringUtils.isNotBlank(implClass)){
            return Configs.commaSplitter.splitToList(implClass);
        }
        return null;
    }

    private String getSpiConfigKey(String className){
        return Configs.ConfigKey.SPI_LOADER_KEY + "." + className;
    }
}
