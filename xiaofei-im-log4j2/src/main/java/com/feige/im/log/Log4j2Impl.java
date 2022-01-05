package com.feige.im.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author feige<br />
 * @ClassName: Log4j2Impl <br/>
 * @Description: <br/>
 * @date: 2022/1/5 10:23<br/>
 */
public class Log4j2Impl implements com.feige.im.log.Logger {

    private static final Logger LOG = LogManager.getLogger("XIAOFEI-IM-LOG4J2");


    @Override
    public void info(String format, Object... arguments) {
        LOG.info(new Throwable().getStackTrace()[1] + " - " + format, arguments);
    }

    @Override
    public void debugInfo(String format, Object... arguments) {
        if (LOG.isDebugEnabled()){
            LOG.info(new Throwable().getStackTrace()[1] + " - " + format, arguments);
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        LOG.warn(new Throwable().getStackTrace()[1] + " - " + format,arguments);
    }

    @Override
    public void debugWarn(String format, Object... arguments) {
        if (LOG.isDebugEnabled()){
            LOG.warn(new Throwable().getStackTrace()[1] + " - " + format, arguments);
        }
    }


    @Override
    public void trace(String format, Object... arguments) {
        LOG.trace(new Throwable().getStackTrace()[1] + " - " + format, arguments);
    }

    @Override
    public void debugTrace(String format, boolean isDebug, Object... arguments) {
        if (LOG.isDebugEnabled()) {
            LOG.trace(new Throwable().getStackTrace()[1] + " - " + format,arguments);
        }
    }


    @Override
    public void debug(String format, Object... arguments) {
        LOG.debug(new Throwable().getStackTrace()[1] + " - " + format, arguments);
    }

    @Override
    public void error(String format, Object... arguments) {
        LOG.error(new Throwable().getStackTrace()[1] + " - " + format,arguments);
    }

    @Override
    public void debugError(String format, Object... arguments) {
        if (LOG.isDebugEnabled()){
            LOG.error(new Throwable().getStackTrace()[1] + " - " + format, arguments);
        }
    }

    @Override
    public void error(String msg, Throwable e) {
        LOG.error(msg,e);
    }

    @Override
    public void debugError(String format, Throwable e) {
        if (LOG.isDebugEnabled()) {
            LOG.error(format, e);
        }
    }

}
