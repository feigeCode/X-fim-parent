package com.feige.im.route;

import java.util.Collection;
import java.util.List;

/**
 * @author feige<br />
 * @ClassName: IRoute <br/>
 * @Description: 路由接口，方便实现多种路由策略<br/>
 * @date: 2021/10/27 22:43<br/>
 */
public interface IRoute {

    /**
     * 添加集群服务节点IP
     * @param servers
     */
    void add(Collection<String> servers);

    default String getRoute(List<String> servers,String key){
        return getRoute(key);
    };

    /**
     * 通过接收者获取对方所在节点的IP
     * @param key
     * @return
     */
    String getRoute(String key);

    /**
     * 移除IP
     * @param server
     */
    void remove(String server);

    default int hash(String str){
        int hashCode = str.hashCode();
        return hashCode >= 0 ? hashCode : Math.abs(hashCode);
    }
}
