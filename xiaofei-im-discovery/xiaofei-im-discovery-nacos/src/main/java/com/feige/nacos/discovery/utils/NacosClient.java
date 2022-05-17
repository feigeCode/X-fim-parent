package com.feige.nacos.discovery.utils;

import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;

import java.util.Properties;

/**
 * @author feige<br />
 * @ClassName: NacosClient <br/>
 * @Description: <br/>
 * @date: 2021/11/11 11:31<br/>
 */
public class NacosClient {

    private static final Logger LOG = LoggerFactory.getLogger();
    private static final Properties prop = new Properties();
    static {
        prop.setProperty("serverAddr", "121.37.216.145:8848");
    }

    public static NamingService getNamingService(){
        try {
            return NamingFactory.createNamingService(prop);
        } catch (NacosException e) {
            LOG.error("nacos NamingService error:",e);
        }
        return null;
    }

    public static ConfigService getConfigService(){
        try {
            return ConfigFactory.createConfigService(prop);
        } catch (NacosException e) {
            LOG.error("nacos ConfigService error:",e);
        }
        return null;
    }

    public static NamingService getNamingService(Properties properties){
        try {
            return NamingFactory.createNamingService(properties);
        } catch (NacosException e) {
            LOG.error("nacos NamingService error:",e);
        }
        return null;
    }

    public static ConfigService getConfigService(Properties properties){
        try {
            return ConfigFactory.createConfigService(properties);
        } catch (NacosException e) {
            LOG.error("nacos ConfigService error:",e);
        }
        return null;
    }

}
