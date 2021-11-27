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
    public static final List<ProviderService> PROVIDER_SERVICES = new ArrayList<>();

    public static ProviderService getProviderService(){
        if (PROVIDER_SERVICES.isEmpty()){
            synchronized (DiscoveryManager.class){
                if (PROVIDER_SERVICES.isEmpty()) {
                    Iterator<ProviderService> iterator = providerServices.iterator();
                    if (iterator.hasNext()){
                        PROVIDER_SERVICES.add(iterator.next());
                    }
                }
            }
        }
        if (PROVIDER_SERVICES.size() > 0){
            return PROVIDER_SERVICES.get(0);
        }
        throw new IllegalArgumentException("未发现任何的注册中心实现类，请检查META-INF/services下的配置文件");
    }
}
