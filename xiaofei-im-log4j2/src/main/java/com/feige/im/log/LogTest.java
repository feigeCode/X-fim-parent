package com.feige.im.log;

/**
 * @author feige<br />
 * @ClassName: LogTest <br/>
 * @Description: <br/>
 * @date: 2022/1/5 11:10<br/>
 */
public class LogTest {

    private static final Logger LOGGER = LoggerFactory.getLogger();

    public static void info(){
        LOGGER.info("日志a={},b={}", "a", "b");
    }

    public static void error(){
        try {
            int i = 1 / 0;
        }catch (Exception e){
            LOGGER.error("error:",e);
        }
    }
}
