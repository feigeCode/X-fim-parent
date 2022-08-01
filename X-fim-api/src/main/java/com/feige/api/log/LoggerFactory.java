package com.feige.api.log;

/**
 * @author feige<br />
 * @ClassName: LoggerFactory <br/>
 * @Description: <br/>
 * @date: 2022/8/1 22:41<br/>
 */
public interface LoggerFactory {
    
    Logger create(String loggerName, String fileNamePattern);
}
