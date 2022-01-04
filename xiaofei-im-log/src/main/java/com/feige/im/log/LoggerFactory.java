package com.feige.im.log;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author feige<br />
 * @ClassName: LoggerFactory <br/>
 * @Description: <br/>
 * @date: 2022/1/2 15:53<br/>
 */
public final class LoggerFactory {

    private static Logger LOGGER;
    static {
        ServiceLoader<Logger> loggerLoader = ServiceLoader.load(Logger.class);
        Iterator<Logger> iterator = loggerLoader.iterator();
        if (iterator.hasNext()) {
            LOGGER = iterator.next();
        }

        if (LOGGER == null){
            throw new RuntimeException("未发现任何的路由实现类，请检查META-INF/services下的配置文件");
        }
    }

    public static Logger getLogger(){
        return LOGGER;
    }

}
