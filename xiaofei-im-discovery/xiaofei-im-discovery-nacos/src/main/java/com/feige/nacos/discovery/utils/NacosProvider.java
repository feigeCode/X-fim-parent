package com.feige.nacos.discovery.utils;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.naming.NamingService;

/**
 * @author feige<br />
 * @ClassName: NacosProvider <br/>
 * @Description: <br/>
 * @date: 2022/5/8 10:44<br/>
 */
public interface NacosProvider {

    /**
     * 获取NamingService
     * @return
     */
    NamingService getNamingService();

    /**
     * 获取ConfigService
     * @return
     */
    ConfigService getConfigService();
}
