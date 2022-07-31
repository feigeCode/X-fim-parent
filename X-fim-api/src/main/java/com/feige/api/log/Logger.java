package com.feige.api.log;

/**
 * @author feige<br />
 * @ClassName: Logger <br/>
 * @Description: <br/>
 * @date: 2022/1/2 16:03<br/>
 */
public interface Logger {

    /**
     * 记录info级别日志
     * @param format
     * @param arguments
     */
    void info(String format, Object... arguments);


    /**
     * 记录info级别日志，debug模式下纪录
     * @param format
     * @param arguments
     */
    void debugInfo(String format, Object... arguments);

    /**
     * 记录warn级别日志
     * @param format
     * @param arguments
     */
    void warn(String format, Object... arguments);


    /**
     * 记录warn级别日志，debug模式下纪录
     * @param format
     * @param arguments
     */
    void debugWarn(String format, Object... arguments);


    /**
     * 记录trace级别日志
     * @param format
     * @param arguments
     */
    void trace(String format, Object... arguments);


    /**
     * 记录trace级别日志，debug模式下纪录
     * @param format
     * @param arguments
     */
    void debugTrace(String format,boolean isDebug, Object... arguments);


    /**
     * 记录error级别日志
     * @param format
     * @param arguments
     */
    void debug(String format, Object... arguments);


    /**
     * 记录error级别日志
     * @param format
     * @param arguments
     */
    void error(String format, Object... arguments);


    /**
     * 记录error级别日志，debug模式下纪录
     * @param format
     * @param arguments
     */
    void debugError(String format, Object... arguments);

    /**
     * 记录error级别异常日志
     * @param msg
     * @param e
     */
    void error(String msg, Throwable e);


    /**
     * 记录error级别异常日志，debug模式下纪录
     * @param format
     * @param e
     */
    void debugError(String format, Throwable e);

}
