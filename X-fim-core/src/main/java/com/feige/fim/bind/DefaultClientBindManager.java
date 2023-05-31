package com.feige.fim.bind;

import com.feige.api.bind.ClientBindInfo;
import com.feige.api.bind.ClientBindManager;
import com.feige.api.bind.ClientType;
import com.feige.api.cache.CacheGroup;
import com.feige.api.cache.CacheManager;
import com.feige.api.cache.MapCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author feige<br />
 * @ClassName: DefaultClientBindManager <br/>
 * @Description: <br/>
 * @date: 2023/5/27 10:47<br/>
 */
public class DefaultClientBindManager extends AbstractClientBindManager {
    public DefaultClientBindManager(CacheManager cacheManager) {
        super(cacheManager);
    }

    public MapCache<Integer, ClientBindInfo> get(String clientId){
        CacheGroup group = cacheManager.getGroup(ClientBindManager.class);
        MapCache<Integer, ClientBindInfo> mapCache = group.get(clientId, MapCache.class);
        if (mapCache == null){
            return group.createMap(clientId, Integer.class, ClientBindInfo.class);
        }
        return mapCache;
    }
    
    @Override
    public void register(ClientBindInfo clientBindInfo) {
        get(clientBindInfo.getClientId()).put(clientBindInfo.getOsCode(), clientBindInfo);
    }

    @Override
    public ClientBindInfo lookup(String clientId, ClientType clientType) {
        return get(clientId).get(clientType.getCode());
    }

    @Override
    public List<ClientBindInfo> lookupAll(String clientId) {
        Set<Integer> keys = Arrays.stream(ClientType.values())
                .map(ClientType::getCode)
                .collect(Collectors.toSet());
        return new ArrayList<>(get(clientId).getAll(keys).values());
    }

    @Override
    public ClientBindInfo unregister(String clientId, ClientType clientType) {
        MapCache<Integer, ClientBindInfo> mapCache = get(clientId);
        ClientBindInfo clientBindInfo = mapCache.remove(clientType.getCode());
        
        return clientBindInfo;
    }
}
