package com.feige.fim.lg;

import com.feige.fim.api.Log;
import com.feige.utils.logger.Loggers;

/**
 * @author feige<br />
 * @ClassName: ConsoleLog <br/>
 * @Description: <br/>
 * @date: 2023/6/4 15:43<br/>
 */
public class ConsoleLog implements Log {
    @Override
    public void debug(String msg, Object... args) {
        Loggers.CONSOLE.debug(msg, args);
    }

    @Override
    public void info(String msg, Object... args) {
        Loggers.CONSOLE.info(msg, args);
    }

    @Override
    public void warn(String msg, Object... args) {
        Loggers.CONSOLE.warn(msg, args);
    }

    @Override
    public void error(String msg, Throwable throwable) {
       Loggers.CONSOLE.error(msg, throwable);
    }
}
