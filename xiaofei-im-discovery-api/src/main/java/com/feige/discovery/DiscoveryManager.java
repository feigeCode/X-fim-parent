package com.feige.discovery;

import java.security.AccessController;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author feige<br />
 * @ClassName: DiscoveryManager <br/>
 * @Description: <br/>
 * @date: 2021/11/11 21:36<br/>
 */
public class DiscoveryManager {
    private static  final ServiceLoader<ProviderService> providerServices = ServiceLoader.load(ProviderService.class);;


    public static ProviderService getProviderService(){
        Iterator<ProviderService> iterator = providerServices.iterator();
        if (iterator.hasNext()){
            return iterator.next();
        }
        return null;
    }
}
