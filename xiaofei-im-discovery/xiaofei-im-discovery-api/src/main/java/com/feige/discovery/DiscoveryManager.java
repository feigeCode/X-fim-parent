package com.feige.discovery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author feige<br />
 * @ClassName: DiscoveryManager <br/>
 * @Description: <br/>
 * @date: 2021/11/11 21:36<br/>
 */
public class DiscoveryManager {
    private static  final ServiceLoader<ProviderService> providerServices = ServiceLoader.load(ProviderService.class);;
    private static volatile ProviderService providerService;

    public static ProviderService getProviderService(){
        if (providerService == null){
            synchronized (DiscoveryManager.class){
                if (providerService == null) {
                    Iterator<ProviderService> iterator = providerServices.iterator();
                    if (iterator.hasNext()){
                        providerService = iterator.next();
                    }
                }
            }
        }
        if (providerService == null){
            throw new IllegalArgumentException("未发现任何的注册中心实现类，请检查META-INF/services下的配置文件");
        }
        return providerService;
    }
}
