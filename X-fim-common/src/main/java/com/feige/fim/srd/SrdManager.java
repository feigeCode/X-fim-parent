package com.feige.fim.srd;

import com.feige.api.srd.IServiceRegistryAndDiscovery;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author feige<br />
 * @ClassName: DiscoveryManager <br/>
 * @Description: <br/>
 * @date: 2021/11/11 21:36<br/>
 */
public final class SrdManager {

    private static IServiceRegistryAndDiscovery providerService;

    static  {
        ServiceLoader<IServiceRegistryAndDiscovery> providerServices = ServiceLoader.load(IServiceRegistryAndDiscovery.class);
        Iterator<IServiceRegistryAndDiscovery> iterator = providerServices.iterator();
        if (iterator.hasNext()){
            providerService = iterator.next();
        }

        if (providerService == null){
            throw new IllegalArgumentException("未发现任何的注册中心实现类，请检查META-INF/services下的配置文件");
        }
    }

    public static IServiceRegistryAndDiscovery getProviderService(){
        return providerService;
    }
}
