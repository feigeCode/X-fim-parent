package com.feige.im.route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author feige<br />
 * @ClassName: RouteManager <br/>
 * @Description: <br/>
 * @date: 2021/11/11 21:58<br/>
 */
public class RouteManager {

    private static  final ServiceLoader<IRoute> iRoutes = ServiceLoader.load(IRoute.class);;
    public static final List<IRoute> ROUTES = new ArrayList<>();

    public static IRoute getIRoutes(){
        if (ROUTES.isEmpty()){
            synchronized (RouteManager.class){
                if (ROUTES.isEmpty()) {
                    Iterator<IRoute> iterator = iRoutes.iterator();
                    if (iterator.hasNext()){
                        ROUTES.add(iterator.next());
                    }
                }
            }
        }
        if (ROUTES.size() > 0){
            return ROUTES.get(0);
        }
        throw new IllegalArgumentException("未发现任何的路由实现类，请检查META-INF/services下的配置文件");
    }
}
