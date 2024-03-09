package com.feige.fim;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.feige.fim.constant.ServerConfigKey;
import com.feige.framework.utils.Configs;
import com.feige.utils.logger.Loggers;

import java.util.Properties;

public class NacosClient {
    public static final String SERVER_ADDR_KEY = "serverAddr";
    private static NamingService namingService;
   
   
   
   public static NamingService getNamingService(){
       try {
           if (namingService == null){
               Properties prop = new Properties();
               String serverList = Configs.getString(ServerConfigKey.NACOS_SERVER_LIST_KEY);
               prop.setProperty(SERVER_ADDR_KEY, serverList);
               namingService = NamingFactory.createNamingService(prop);
           }
       } catch (NacosException e) {
           Loggers.SRD.error("nacos start failure: ", e);
           throw new RuntimeException(e);
       }
       return namingService;
   }
}
