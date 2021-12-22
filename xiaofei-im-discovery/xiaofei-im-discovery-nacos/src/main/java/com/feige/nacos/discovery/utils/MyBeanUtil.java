package com.feige.nacos.discovery.utils;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.feige.discovery.pojo.ServerInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author feige<br />
 * @ClassName:  MyBeanUtil<br/>
 * @Description: <br/>
 * @date: 2021/11/11 13:41<br/>
 */
public class MyBeanUtil {

    private static final Logger LOG = LogManager.getLogger(MyBeanUtil.class);


    public static Instance serverInstance2Instance(ServerInstance serverInstance){
        if (serverInstance == null){
            LOG.info("serverInstance不能为空");
            throw new IllegalArgumentException("serverInstance不能为空");
        }
        Instance instance = new Instance();
        instance.setInstanceId(serverInstance.getInstanceId());
        instance.setPort(serverInstance.getPort());
        instance.setIp(serverInstance.getIp());
        instance.setEnabled(serverInstance.isEnabled());
        instance.setEphemeral(serverInstance.isEphemeral());
        instance.setClusterName(serverInstance.getClusterName());
        instance.setHealthy(serverInstance.isHealthy());
        instance.setWeight(serverInstance.getWeight());
        instance.setMetadata(serverInstance.getMetadata());
        instance.setServiceName(serverInstance.getServiceName());
        return instance;
    }



    public static ServerInstance instance2serverInstance(Instance instance){
        if (instance == null){
            LOG.info("instance");
            throw new IllegalArgumentException("instance不能为空");
        }
        ServerInstance serverInstance = new ServerInstance();
        serverInstance.setInstanceId(instance.getInstanceId());
        serverInstance.setPort(instance.getPort());
        serverInstance.setIp(instance.getIp());
        serverInstance.setEnabled(instance.isEnabled());
        serverInstance.setEphemeral(instance.isEphemeral());
        serverInstance.setClusterName(instance.getClusterName());
        serverInstance.setHealthy(instance.isHealthy());
        serverInstance.setWeight(instance.getWeight());
        serverInstance.setMetadata(instance.getMetadata());
        serverInstance.setServiceName(instance.getServiceName());
        return serverInstance;
    }

    public static List<ServerInstance> instances2serverInstances(List<Instance> instances){
        List<ServerInstance> serverInstances = new ArrayList<>();
        for (Instance instance : instances) {
            serverInstances.add(instance2serverInstance(instance));
        }
        return serverInstances;
    }
}
