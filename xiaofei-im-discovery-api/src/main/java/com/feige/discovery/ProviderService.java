package com.feige.discovery;

import com.feige.discovery.pojo.ServerInstance;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: ProviderService <br/>
 * @Description: <br/>
 * @date: 2021/11/4 23:26<br/>
 */
public interface ProviderService {

    String CLUSTER_NAME = "XIAOFEI-IM";


    /**
     * 注册实例
     * @param serverInstance 服务实例
     */
    void registerServerInstance(ServerInstance serverInstance);


    /**
     * 注销实例
     * @param serverInstance 服务实例
     */
    void deregisterServerInstance(ServerInstance serverInstance);


    /**
     * 获取服务所有实例
     * @param serverName
     * @return
     */
    List<ServerInstance> getAllServerInstances();

    /**
     * 监听服务下的实例列表变化
     * @param name
     */
    void subscribe();

}
