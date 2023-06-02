package com.feige.fim.bind;

import com.feige.api.bind.ClientBindInfo;
import com.feige.api.bind.ClientBindManager;
import com.feige.api.bind.ClientType;
import com.feige.api.cache.CacheGroup;
import com.feige.api.cache.CacheManager;
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
public class DefaultClientBindManager extends AbstractClientBindManager {
    
    public DefaultClientBindManager(CacheManager cacheManager) {
        super(cacheManager);
    }

    protected MapCache<String, ClientBindInfo> getMapCache(){
        CacheGroup group = cacheManager.getGroup(ClientBindManager.class);
        String canonicalName = this.getClass().getCanonicalName();
        MapCache<String, ClientBindInfo> mapCache = group.get(canonicalName, MapCache.class);
        if (mapCache == null){
            synchronized (this){
                mapCache = group.get(canonicalName, MapCache.class);
                if (mapCache == null) {
                    mapCache = group.createMap(canonicalName, String.class, ClientBindInfo.class);
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
        mapCache.put(getKey(clientBindInfo.getClientId(), clientBindInfo.getOsCode()), clientBindInfo);
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

    @Override
    public String getKey() {
        return "default";
    }
}
