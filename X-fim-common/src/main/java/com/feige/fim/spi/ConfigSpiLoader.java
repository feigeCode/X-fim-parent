package com.feige.fim.spi;



import com.feige.api.order.OrderComparator;
import com.feige.fim.config.Configs;
import com.feige.fim.utils.StringUtil;

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
        if (StringUtil.isNotBlank(implClass)){
            return Configs.commaSplitter.splitToList(implClass);
        }
        return null;
    }

    private String getSpiConfigKey(String className){
        return Configs.ConfigKey.SPI_LOADER_KEY + "." + className;
    }
}
