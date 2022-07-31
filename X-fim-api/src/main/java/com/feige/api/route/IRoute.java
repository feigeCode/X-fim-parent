package com.feige.api.route;

import java.util.Collection;

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
    default void add(Collection<String> servers){

    }

    /**
     * 用户上线
     * @param userId 用户ID
     * @param address 上线的机器地址
     */
    void online(String userId, String address);


    /**
     * 通过用户ID获取到用户所在机器的地址，用户转发消息
     * @param userId 用户ID
     * @return
     */
    String getRoute(String userId);

    /**
     * 用户下线
     * @param userId 用户ID
     */
    void offline(String userId);

}
