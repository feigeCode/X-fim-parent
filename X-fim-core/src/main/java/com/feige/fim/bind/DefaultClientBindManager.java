package com.feige.fim.bind;

import com.feige.api.bind.AbstractClientBindManager;
import com.feige.api.bind.ClientBindManager;
import com.feige.utils.spi.annotation.SpiComp;
import com.feige.api.bind.ClientBindInfo;
import com.feige.api.constant.ClientType;
import com.feige.api.cache.MapCache;
import com.feige.api.constant.Const;


import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author feige<br />
 * @ClassName: DefaultClientBindManager <br/>
 * @Description: <br/>
 * @date: 2023/5/27 10:47<br/>
 */
@SpiComp(value="default", interfaces = ClientBindManager.class)
public class DefaultClientBindManager extends AbstractClientBindManager {
    
    public static final String CACHE_NAME = "BIND_CLIENT";

    protected MapCache<String, ClientBindInfo> getMapCache(){
        MapCache<String, ClientBindInfo> mapCache = cacheManager.get(CACHE_NAME, MapCache.class);
        if (mapCache == null){
            synchronized (this){
                mapCache = cacheManager.get(CACHE_NAME, MapCache.class);
                if (mapCache == null) {
                    mapCache = cacheManager.createMapCache(CACHE_NAME, String.class, ClientBindInfo.class);
                }
            }
        }
        return mapCache;
    }
    
    protected String getKey(String clientId, int osCode){
        return clientId + Const.UNDERLINE + osCode;
    }
    
    @Override
    public void register(ClientBindInfo clientBindInfo) {
        MapCache<String, ClientBindInfo> mapCache = getMapCache();
        mapCache.put(getKey(clientBindInfo.getClientId(), clientBindInfo.getClientType()), clientBindInfo);
    }

    @Override
    public ClientBindInfo lookup(String clientId, ClientType clientType) {
        String key = getKey(clientId, clientType.getCode());
        MapCache<String, ClientBindInfo> mapCache = getMapCache();
        return mapCache.get(key);
    }

    @Override
    public Map<String, ClientBindInfo> lookupAll(final String clientId) {
        Set<String> keys = Arrays.stream(ClientType.values())
                .map(clientType -> getKey(clientId, clientType.getCode()))
                .collect(Collectors.toSet());
        return getMapCache().getAll(keys);
    }

    @Override
    public ClientBindInfo unregister(String clientId, ClientType clientType) {
        MapCache<String, ClientBindInfo> mapCache = getMapCache();
        String key = getKey(clientId, clientType.getCode());
        return mapCache.remove(key);
    }
    
}
