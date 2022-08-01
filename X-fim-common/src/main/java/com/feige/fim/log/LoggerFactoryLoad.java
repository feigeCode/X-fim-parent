package com.feige.fim.log;

import com.feige.api.log.Logger;
import com.feige.api.log.LoggerFactory;
import com.feige.fim.log.impl.JavaLoggerFactory;

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
public final class LoggerFactoryLoad {

    private static final Map<String, Logger> LOGGER_CACHE = new ConcurrentHashMap<>();
    
    // 默认使用的日志
    private static LoggerFactory LOGGER_FACTORY = new JavaLoggerFactory();


    public static Logger getLogger(String loggerName, String fileNamePattern){
        Logger logger = LOGGER_CACHE.get(loggerName);
        if (logger == null){
            synchronized (LoggerFactoryLoad.class){
                if (logger == null){
                    logger = LOGGER_FACTORY.create(loggerName, fileNamePattern);
                    LOGGER_CACHE.put(loggerName, logger);
                }
            }
        }
        return logger;
    }


    public static Logger getLogger(Class<?> clazz, String fileNamePattern){
        String loggerName = clazz.getCanonicalName();
        Logger logger = LOGGER_CACHE.get(loggerName);
        if (logger == null){
            logger = LOGGER_FACTORY.create(loggerName, fileNamePattern);
        }
        return logger;
    }

    public static synchronized void load(){
        LoggerFactory lf = null;
        try {
            ServiceLoader<LoggerFactory> loggerLoader = ServiceLoader.load(LoggerFactory.class);
            Iterator<LoggerFactory> iterator = loggerLoader.iterator();
            if (iterator.hasNext()) {
                lf = iterator.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lf != null){
            LOGGER_FACTORY = lf;
        }
    }

}
