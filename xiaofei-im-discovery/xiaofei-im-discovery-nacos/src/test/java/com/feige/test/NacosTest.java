package com.feige.test;

import com.feige.discovery.DiscoveryManager;
import com.feige.discovery.ProviderService;
import com.feige.discovery.pojo.ServerInstance;

/**
 * @author feige<br />
 * @ClassName: NacosTest <br/>
 * @Description: <br/>
 * @date: 2021/11/16 23:34<br/>
 */
public class NacosTest {

    public static void main(String[] args) {
        ProviderService providerService = DiscoveryManager.getProviderService();
        assert providerService != null;
        ServerInstance instance = new ServerInstance("127.0.0.1", 8090);
        instance.setServiceName("test");
        providerService.registerServerInstance(instance);
    }
}
