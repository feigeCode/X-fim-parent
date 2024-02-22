package com.feige.fim.lg;

import com.feige.fim.api.Log;

/**
 * @author feige<br />
 * @ClassName: Logs <br/>
 * @Description: <br/>
 * @date: 2023/6/4 15:33<br/>
 */
public class Logs {
    
    private Log log = new ConsoleLog();
    
    public void debug(String msg, Object... args) {
        log.debug(msg, args);
    }

    public void info(String msg, Object... args) {
        log.info(msg, args);
    }

    public void warn(String msg, Object... args) {
        log.warn(msg, args);
    }

    public void error(String msg, Throwable throwable) {
        log.error(msg, throwable);
    }
    
    public static Logs getInstance(){
        return LogsInner.LOGS;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    static class LogsInner {
        private static final Logs LOGS = new Logs();
    }
}
