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

    private static NamingService namingService = NacosClient.getNamingService();

    private static ConfigService configService = NacosClient.getConfigService();

    public static void setNamingService(NamingService namingService){
        if (namingService == null) {
            throw new IllegalArgumentException("namingService is null");
        }
        NacosProviderUtil.namingService = namingService;
    }

    public static void setConfigService(ConfigService configService){
        if (configService == null) {
            throw new IllegalArgumentException("configService is null");
        }
        NacosProviderUtil.configService = configService;
    }


    public static NamingService getNamingService(){
        return namingService;
    }

    public static ConfigService getConfigService(){
        return configService;
    }



}
