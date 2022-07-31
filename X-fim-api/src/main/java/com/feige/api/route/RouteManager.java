package com.feige.api.route;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author feige<br />
 * @ClassName: RouteManager <br/>
 * @Description: <br/>
 * @date: 2021/11/11 21:58<br/>
 */
public final class RouteManager {

    private static IRoute route;

    static {
        ServiceLoader<IRoute> iRoutes = ServiceLoader.load(IRoute.class);
        Iterator<IRoute> iterator = iRoutes.iterator();
        if (iterator.hasNext()){
            route = iterator.next();
        }
        if (route == null){
            throw new RuntimeException("未发现任何的路由实现类，请检查META-INF/services下的配置文件");
        }
    }

    public static IRoute getIRoutes(){
        return route;
    }
}
