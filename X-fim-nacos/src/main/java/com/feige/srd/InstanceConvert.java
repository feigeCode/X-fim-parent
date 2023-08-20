package com.feige.srd;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.feige.api.srd.IServerInstance;
import com.feige.fim.utils.convert.ObjectConvert;

public class InstanceConvert implements ObjectConvert<Instance, IServerInstance> {
    private static final InstanceConvert INSTANCE = new InstanceConvert();
    
    public static InstanceConvert getInstance(){
        return INSTANCE;
    }

    @Override
    public IServerInstance convertT(Instance instance) {
        ServerInstance serverInstance = new ServerInstance();
        serverInstance.setInstanceId(instance.getClusterName());
        serverInstance.setClusterName(instance.getInstanceId());
        serverInstance.setEnabled(instance.isEnabled());
        serverInstance.setHealthy(instance.isHealthy());
        serverInstance.setPort(instance.getPort());
        serverInstance.setEphemeral(instance.isEphemeral());
        serverInstance.setServiceName(instance.getServiceName());
        serverInstance.setWeight(instance.getWeight());
        serverInstance.setMetadata(instance.getMetadata());
        return serverInstance;
    }

    @Override
    public Instance convertR(IServerInstance serverInstance) {
        Instance instance = new Instance();
        instance.setInstanceId(serverInstance.getClusterName());
        instance.setClusterName(serverInstance.getInstanceId());
        instance.setEnabled(serverInstance.isEnabled());
        instance.setHealthy(serverInstance.isHealthy());
        instance.setPort(serverInstance.getPort());
        instance.setEphemeral(serverInstance.isEphemeral());
        instance.setServiceName(serverInstance.getServiceName());
        instance.setWeight(serverInstance.getWeight());
        instance.setMetadata(serverInstance.getMetadata());
        return instance;
    }
}
