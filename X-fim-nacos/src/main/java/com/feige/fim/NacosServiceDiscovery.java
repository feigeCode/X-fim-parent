package com.feige.fim;

import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.feige.api.sc.Callback;
import com.feige.api.srd.ServerInstance;
import com.feige.api.srd.ServiceDiscovery;
import com.feige.srd.InstanceConvert;
import com.feige.utils.logger.Loggers;
import com.feige.utils.spi.annotation.SPI;

import java.util.ArrayList;
import java.util.List;

@SPI(value = "nacosServiceDiscovery", interfaces = ServiceDiscovery.class)
public class NacosServiceDiscovery implements ServiceDiscovery {
    @Override
    public List<ServerInstance> getAllServerInstances(String serverName) {
        try {
            List<ServerInstance> serverInstances = new ArrayList<>();
            List<Instance> allInstances = NacosClient.getNamingService().getAllInstances(serverName);
            for (Instance instance : allInstances) {
                ServerInstance serverInstance = InstanceConvert.getInstance().convertT(instance);
                serverInstances.add(serverInstance);
            }
            return serverInstances;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subscribe(String serverName, Callback<List<ServerInstance>> callback) {
        try {
            NacosClient.getNamingService().subscribe(serverName, (event) -> {
                if (event instanceof NamingEvent){
                    List<ServerInstance> serverInstances = new ArrayList<>();
                    NamingEvent namingEvent = (NamingEvent) event;
                    List<Instance> instances = namingEvent.getInstances();
                    for (Instance instance : instances) {
                        ServerInstance serverInstance = InstanceConvert.getInstance().convertT(instance);
                        serverInstances.add(serverInstance);
                    }
                    Loggers.SRD.info("Nacos subscribe NamingEvent: instances = {}",  instances);
                    if (callback != null){
                        callback.call(serverInstances);
                    }
                }
            });
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unsubscribe(String serverName, Callback<List<ServerInstance>> callback) {
        try {
            NacosClient.getNamingService().unsubscribe(serverName, (event) -> {
                if (event instanceof NamingEvent){
                    List<ServerInstance> serverInstances = new ArrayList<>();
                    NamingEvent namingEvent = (NamingEvent) event;
                    List<Instance> instances = namingEvent.getInstances();
                    for (Instance instance : instances) {
                        ServerInstance serverInstance = InstanceConvert.getInstance().convertT(instance);
                        serverInstances.add(serverInstance);
                    }
                    Loggers.SRD.info("Nacos unsubscribe NamingEvent: instances = {}",  instances);
                    if (callback != null){
                        callback.call(serverInstances);
                    }
                }
            });
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
