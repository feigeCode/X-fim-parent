package com.feige.discovery;

import sun.security.jca.Providers;

import javax.xml.ws.Provider;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author feige<br />
 * @ClassName: DiscoveryManager <br/>
 * @Description: <br/>
 * @date: 2021/11/11 21:36<br/>
 */
public final class DiscoveryManager {

    private static ProviderService providerService;

    static  {
        ServiceLoader<ProviderService> providerServices = ServiceLoader.load(ProviderService.class);
        Iterator<ProviderService> iterator = providerServices.iterator();
        if (iterator.hasNext()){
            providerService = iterator.next();
        }

        if (providerService == null){
            throw new IllegalArgumentException("未发现任何的注册中心实现类，请检查META-INF/services下的配置文件");
        }
    }

    public static ProviderService getProviderService(){
        return providerService;
    }
}
