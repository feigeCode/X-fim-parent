package com.feige.fim.log;

import com.feige.api.log.Logger;
import com.feige.fim.log.impl.jul.JavaLoggingAdapter;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author feige<br />
 * @ClassName: LoggerFactory <br/>
 * @Description: <br/>
 * @date: 2022/1/2 15:53<br/>
 */
public final class LoggerFactory {

    private static Map<String, Logger> LOGGER_CACHE = new ConcurrentHashMap<>();


    public static Logger getLogger(String loggerName, String fileNamePattern){
        Logger logger = LOGGER_CACHE.get(loggerName);
        if (logger == null){
            logger = load(loggerName, fileNamePattern);
        }
        return logger;
    }

    public static synchronized Logger load(String loggerName, String fileNamePattern){
        // 默认使用的日志
        Logger logger = new JavaLoggingAdapter(loggerName, fileNamePattern);
        try {
            ServiceLoader<Logger> loggerLoader = ServiceLoader.load(Logger.class);
            Iterator<Logger> iterator = loggerLoader.iterator();
            if (iterator.hasNext()) {
                logger = iterator.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER_CACHE.put(loggerName, logger);

        return logger;
    }

}
