package com.feige.api.srd;

public interface ServiceRegistry {

    /**
     * 注册实例
     * @param serverInstance 服务实例
     */
    void registerIServerInstance(ServerInstance serverInstance);


    /**
     * 注销实例
     * @param serverInstance 服务实例
     */
    void deregisterIServerInstance(ServerInstance serverInstance);
}
