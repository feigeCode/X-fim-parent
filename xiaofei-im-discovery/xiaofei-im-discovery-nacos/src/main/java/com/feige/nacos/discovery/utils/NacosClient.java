package com.feige.nacos.discovery.utils;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;

/**
 * @author feige<br />
 * @ClassName: NacosClient <br/>
 * @Description: <br/>
 * @date: 2021/11/11 11:31<br/>
 */
public class NacosClient {

    private static final Logger LOG = LoggerFactory.getLogger();

    public static NamingService getNamingService(){
        try {
            return NamingFactory.createNamingService("127.0.0.1:8848");
        } catch (NacosException e) {
            LOG.error("nacos error:",e);
        }
        return null;
    }
}
