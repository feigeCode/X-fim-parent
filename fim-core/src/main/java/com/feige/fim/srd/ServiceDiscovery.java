package com.feige.fim.srd;

import com.feige.api.sc.Callback;

import java.util.List;

public interface ServiceDiscovery {

    /**
     * 获取服务所有实例
     * @param serverName 服务名
     * @return
     */
    List<ServerInstance> getAllServerInstances(String serverName);

    /**
     * 监听服务下的实例列表变化
     * @param callback 回调
     * @param serverName 服务名
     */
    void subscribe(String serverName, Callback<List<ServerInstance>> callback);


    /**
     * 取消订阅
     * @param serverName 服务名
     * @param callback 回调
     */
    void unsubscribe(String serverName, Callback<List<ServerInstance>> callback);
    
}
