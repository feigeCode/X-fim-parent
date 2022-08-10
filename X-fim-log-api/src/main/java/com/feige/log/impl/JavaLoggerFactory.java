package com.feige.log.impl;

import com.feige.log.Logger;
import com.feige.log.LoggerFactory;
import com.feige.log.impl.jul.JavaLoggingAdapter;

/**
 * @author feige<br />
 * @ClassName: JavaLoggerFactory <br/>
 * @Description: <br/>
 * @date: 2022/8/1 22:57<br/>
 */
public class JavaLoggerFactory implements LoggerFactory {
    @Override
    public Logger create(String loggerName, String fileNamePattern) {
        return new JavaLoggingAdapter(loggerName, fileNamePattern);
    }
}
