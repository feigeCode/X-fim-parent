package com.feige.fim;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.feige.api.srd.ServerInstance;
import com.feige.api.srd.ServiceRegistry;
import com.feige.utils.logger.Loggers;
import com.feige.utils.spi.annotation.SPI;
import com.feige.srd.InstanceConvert;

@SPI(value = "nacosServiceRegistry", interfaces = ServiceRegistry.class)
public class NacosServiceRegistry implements ServiceRegistry {
    @Override
    public void registerIServerInstance(ServerInstance serverInstance) {
        try {
            Loggers.SRD.info("nacos registry, {} {}:{} register begin", serverInstance.getInstanceId(), serverInstance.getIp(), serverInstance.getPort());
            Instance instance = InstanceConvert.getInstance().convertR(serverInstance);
            NacosClient.getNamingService().registerInstance(instance.getServiceName(), instance);
            Loggers.SRD.info("nacos registry, {} {}:{} register finished", instance.getInstanceId(), instance.getIp(), instance.getPort());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deregisterIServerInstance(ServerInstance serverInstance) {
        try {
            Instance instance = InstanceConvert.getInstance().convertR(serverInstance);
            NacosClient.getNamingService().deregisterInstance(instance.getServiceName(), instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
}
