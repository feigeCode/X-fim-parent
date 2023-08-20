package com.feige.fim;

import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.feige.api.sc.Callback;
import com.feige.api.srd.IServerInstance;
import com.feige.api.srd.ServiceRegistryAndDiscovery;
import com.feige.fim.utils.lg.Loggers;
import com.feige.framework.annotation.SpiComp;
import com.feige.srd.InstanceConvert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpiComp(value = "nacos", interfaces = ServiceRegistryAndDiscovery.class)
public class NacosServiceRegistryAndDiscovery implements ServiceRegistryAndDiscovery {
    @Override
    public void registerIServerInstance(IServerInstance serverInstance) {
        try {
            Loggers.SRD.info("nacos registry, {} {}:{} register begin", serverInstance.getInstanceId(), serverInstance.getIp(), serverInstance.getPort());
            Instance instance = InstanceConvert.getInstance().convertR(serverInstance);
            NacosClient.getNamingService().registerInstance(NAME, instance);
            Loggers.SRD.info("nacos registry, {} {}:{} register finished", instance.getInstanceId(), instance.getIp(), instance.getPort());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deregisterIServerInstance(IServerInstance serverInstance) {
        try {
            Instance instance = InstanceConvert.getInstance().convertR(serverInstance);
            NacosClient.getNamingService().deregisterInstance(NAME, instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<IServerInstance> getAllServerInstances() {
        try {
            List<IServerInstance> serverInstances = new ArrayList<>();
            List<Instance> allInstances = NacosClient.getNamingService().getAllInstances(NAME);
            for (Instance instance : allInstances) {
                IServerInstance serverInstance = InstanceConvert.getInstance().convertT(instance);
                serverInstances.add(serverInstance);
            }
            return serverInstances;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subscribe(Callback<List<IServerInstance>> callback) {
        try {
            NacosClient.getNamingService().subscribe(NAME, (event) -> {
                if (event instanceof NamingEvent){
                    List<IServerInstance> serverInstances = new ArrayList<>();
                    NamingEvent namingEvent = (NamingEvent) event;
                    List<Instance> instances = namingEvent.getInstances();
                    for (Instance instance : instances) {
                        IServerInstance serverInstance = InstanceConvert.getInstance().convertT(instance);
                        serverInstances.add(serverInstance);
                    }
                    Loggers.SRD.info("Nacos NamingEvent: instances = {}",  instances);
                    callback.call(serverInstances, Collections.emptyMap());
                }
            });
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
