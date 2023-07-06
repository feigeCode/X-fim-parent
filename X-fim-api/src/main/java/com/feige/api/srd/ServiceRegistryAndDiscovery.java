package com.feige.api.srd;

import com.feige.api.sc.Listener;
import com.feige.api.spi.Spi;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: ProviderService <br/>
 * @Description: <br/>
 * @date: 2021/11/4 23:26<br/>
 */
public interface ServiceRegistryAndDiscovery extends Spi {
    
    /**
     * 注册实例
     * @param serverInstance 服务实例
     */
    void registerIServerInstance(IServerInstance serverInstance);


    /**
     * 注销实例
     * @param serverInstance 服务实例
     */
    void deregisterIServerInstance(IServerInstance serverInstance);


    /**
     * 获取服务所有实例
     * @return
     */
    List<IServerInstance> getAllServerInstances();

    /**
     * 监听服务下的实例列表变化
     * @param listener
     */
    void subscribe(Listener listener);

}
