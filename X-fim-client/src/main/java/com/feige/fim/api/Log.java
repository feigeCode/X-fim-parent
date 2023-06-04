package com.feige.fim.api;

/**
 * @author feige<br />
 * @ClassName: Log <br/>
 * @Description: <br/>
 * @date: 2023/6/4 15:32<br/>
 */
public interface Log {

    void debug(String msg, Object... args);
    
    void info(String msg, Object... args);

    void warn(String msg, Object... args);

    void error(String msg, Throwable throwable);
    
    
}
