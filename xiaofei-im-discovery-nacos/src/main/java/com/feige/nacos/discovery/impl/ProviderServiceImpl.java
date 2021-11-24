package com.feige.nacos.discovery.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.feige.discovery.ProviderService;
import com.feige.discovery.pojo.ServerInstance;
import com.feige.nacos.discovery.utils.MyBeanUtil;
import com.feige.nacos.discovery.utils.NacosClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author feige<br />
 * @ClassName: ProviderServiceImpl <br/>
 * @Description: <br/>
 * @date: 2021/11/11 11:30<br/>
 */
public class ProviderServiceImpl implements ProviderService {

    private static final Logger LOG = LogManager.getLogger(ProviderServiceImpl.class);

    private final NamingService namingService;

    public ProviderServiceImpl(NamingService namingService) {
        this.namingService = namingService;
    }

    public ProviderServiceImpl() {
        this.namingService = NacosClient.getNamingService();
    }

    @Override
    public void registerServerInstance(ServerInstance serverInstance) {
        Instance instance = MyBeanUtil.serverInstance2Instance(serverInstance);
        try {
            namingService.registerInstance(CLUSTER_NAME,instance);
            LOG.info("nacos registry, {} {}:{} register finished", new Object[]{instance.getInstanceId(), instance.getIp(), instance.getPort()});
        } catch (NacosException e) {
            LOG.error("nacos registry, {} register failed...{},", new Object[]{instance.getInstanceId(), instance.toString()});
            LOG.error(e.getErrMsg(),e);
        }
    }

    @Override
    public void deregisterServerInstance(ServerInstance serverInstance) {
        Instance instance = MyBeanUtil.serverInstance2Instance(serverInstance);
        try {
            namingService.deregisterInstance(CLUSTER_NAME,instance);
        } catch (NacosException e) {
            LOG.error(e);
        }
    }

    @Override
    public List<ServerInstance> getAllServerInstances() {
        try {
            List<Instance> instanceList = namingService.getAllInstances(CLUSTER_NAME);
            return MyBeanUtil.instances2serverInstances(instanceList);
        } catch (NacosException e) {
            LOG.error(e);
        }
        return new ArrayList<>();
    }

    @Override
    public void subscribe() {
        try {
            namingService.subscribe(CLUSTER_NAME,event -> {
                if (event instanceof NamingEvent){
                    NamingEvent namingEvent = (NamingEvent) event;
                    List<Instance> instances = namingEvent.getInstances();
                    instances.forEach(System.out::println);
                }
            });
        } catch (NacosException e) {
            LOG.error(e);
        }
    }
}
