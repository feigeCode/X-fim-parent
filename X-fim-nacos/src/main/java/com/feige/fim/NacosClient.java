package com.feige.fim;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.feige.fim.config.Configs;
import com.feige.fim.utils.lg.Loggers;

import java.util.Properties;

public class NacosClient {
    public static final String SERVER_ADDR_KEY = "serverAddr";
    private static NamingService namingService;
   
   
   
   public static NamingService getNamingService(){
       try {
           Properties prop = new Properties();
           String serverList = Configs.getString(Configs.ConfigKey.NACOS_SERVER_LIST_KEY);
           prop.setProperty(SERVER_ADDR_KEY, serverList);
           if (namingService == null){
               namingService = NamingFactory.createNamingService(prop);
           }
       } catch (NacosException e) {
           Loggers.SRD.error("nacos start failure: ", e);
           throw new RuntimeException(e);
       }
       return namingService;
   }
}
