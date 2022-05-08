package com.feige.nacos.discovery.utils;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.naming.NamingService;

/**
 * @author feige<br />
 * @ClassName: NacosProviderUtil <br/>
 * @Description: <br/>
 * @date: 2022/5/8 10:55<br/>
 */
public class NacosProviderUtil {

    private static NacosProvider nacosProvider = new DefaultNacosProvider();

    public static void setNacosProvider(NacosProvider nacosProvider){
        if (nacosProvider == null) {
            throw new IllegalArgumentException("nacosProvider is null");
        }
        NacosProviderUtil.nacosProvider = nacosProvider;
    }

    public static NacosProvider getNacosProvider(){
        return nacosProvider;
    }


    public static NamingService getNamingService(){
        if (nacosProvider == null){
            throw new IllegalArgumentException("nacosProvider is null");
        }
        return nacosProvider.getNamingService();
    }

    public static ConfigService getConfigService(){
        if (nacosProvider == null){
            throw new IllegalArgumentException("nacosProvider is null");
        }
        return nacosProvider.getConfigService();
    }



}
