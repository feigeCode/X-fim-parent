package com.feige.im.route;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: IRoute <br/>
 * @Description: 路由接口，方便实现多种路由策略<br/>
 * @date: 2021/10/27 22:43<br/>
 */
public interface IRoute {

    default String getRoute(List<String> servers,String key){
        return null;
    };

    String getRoute(String key);


    default int hash(String str){
        int hashCode = str.hashCode();
        return hashCode >= 0 ? hashCode : Math.abs(hashCode);
    }
}
