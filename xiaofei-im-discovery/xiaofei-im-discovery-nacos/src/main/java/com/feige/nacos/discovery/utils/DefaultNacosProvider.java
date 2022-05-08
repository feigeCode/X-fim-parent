package com.feige.nacos.discovery.utils;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.naming.NamingService;

/**
 * @author feige<br />
 * @ClassName: DefaultNacosProvider <br/>
 * @Description: <br/>
 * @date: 2022/5/8 10:53<br/>
 */
public class DefaultNacosProvider implements NacosProvider{

    @Override
    public NamingService getNamingService() {
        return NacosClient.getNamingService();
    }

    @Override
    public ConfigService getConfigService() {
        return NacosClient.getConfigService();
    }
}
