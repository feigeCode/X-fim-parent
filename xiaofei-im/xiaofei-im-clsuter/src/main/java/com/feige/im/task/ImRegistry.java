package com.feige.im.task;

import com.feige.discovery.DiscoveryManager;
import com.feige.discovery.ProviderService;
import com.feige.discovery.pojo.ServerInstance;
import com.feige.im.utils.SnowflakeIdUtil;

/**
 * @author feige<br />
 * @ClassName: ImRegistry <br/>
 * @Description: <br/>
 * @date: 2021/11/27 19:33<br/>
 */
public class ImRegistry implements Runnable{

    private final String ip;
    private final int port;

    public ImRegistry(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        ProviderService providerService = DiscoveryManager.getProviderService();
        ServerInstance instance = new ServerInstance(this.ip, this.port);
        instance.setServiceName(ProviderService.CLUSTER_NAME);
        instance.setInstanceId(SnowflakeIdUtil.generateId().toString());
        providerService.registerServerInstance(instance);
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
