package com.feige.im.log;

import com.feige.im.log.impl.jul.JavaLoggingAdapter;

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
        try {
            ServiceLoader<Logger> loggerLoader = ServiceLoader.load(Logger.class);
            Iterator<Logger> iterator = loggerLoader.iterator();
            if (iterator.hasNext()) {
                LOGGER = iterator.next();
            }

            if (LOGGER == null){
                // 默认使用的日志
                LOGGER = new JavaLoggingAdapter();
            }
        } catch (Exception e) {
            LOGGER = new JavaLoggingAdapter();
        }
    }

    public static Logger getLogger(){
        return LOGGER;
    }

}
