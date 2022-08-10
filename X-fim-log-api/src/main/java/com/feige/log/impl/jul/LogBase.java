
package com.feige.log.impl.jul;



import com.feige.fim.config.Configs;

import java.io.File;


public class LogBase {

    /**
     * Output biz log (e.g. RecordLog and CommandCenterLog) to file.
     */
    public static final String LOG_OUTPUT_TYPE_FILE = "file";
    /**
     * Output biz log (e.g. RecordLog and CommandCenterLog) to console.
     */
    public static final String LOG_OUTPUT_TYPE_CONSOLE = "console";
    public static final String LOG_CHARSET_UTF8 = "utf-8";

    private static final String DIR_NAME = "logs" + File.separator + "im";
    private static final String USER_HOME = "user.home";


    private static boolean logNameUsePid;
    private static String logOutputType;
    private static String logBaseDir;
    private static String logCharSet;

    static {
        try {
            initializeDefault();
            loadLogConfig();
        } catch (Throwable t) {
            System.err.println("[LogBase] FATAL ERROR when initializing logging config");
            t.printStackTrace();
        }
    }
    public static String addSeparator(String dir) {
        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        return dir;
    }

    private static void initializeDefault() {
        logNameUsePid = false;
        logOutputType = LOG_OUTPUT_TYPE_FILE;
        logBaseDir = addSeparator(System.getProperty(USER_HOME)) + DIR_NAME + File.separator;
        logCharSet = LOG_CHARSET_UTF8;
    }

    private static void loadLogConfig() {

        logOutputType = Configs.Log.OUTPUT_TYPE == null ? logOutputType : Configs.Log.OUTPUT_TYPE;
        if (!LOG_OUTPUT_TYPE_FILE.equalsIgnoreCase(logOutputType) && !LOG_OUTPUT_TYPE_CONSOLE.equalsIgnoreCase(logOutputType)) {
            logOutputType = LOG_OUTPUT_TYPE_FILE;
        }
        System.out.println("INFO: X-FIM log output type is: " + logOutputType);

        logCharSet = Configs.Log.CHARSET == null ? logCharSet : Configs.Log.CHARSET;
        System.out.println("INFO: X-FIM log charset is: " + logCharSet);


        logBaseDir = Configs.Log.DIR == null ? logBaseDir : Configs.Log.DIR;
        logBaseDir = addSeparator(logBaseDir);
        File dir = new File(logBaseDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.err.println("ERROR: create X-FIM log base directory error: " + logBaseDir);
            }
        }
        System.out.println("INFO: X-FIM log base directory is: " + logBaseDir);

        logNameUsePid = Configs.Log.USE_PID;
        System.out.println("INFO: X-FIM log name use pid is: " + logNameUsePid);
    }


    /**
     * Whether log file name should contain pid. This switch is configured by  system property.
     *
     * @return true if log file name should contain pid, return true, otherwise false
     */
    public static boolean isLogNameUsePid() {
        return logNameUsePid;
    }

    /**
     * Get the log file base directory path, which is guaranteed ended with {@link File#separator}.
     *
     * @return log file base directory path
     */
    public static String getLogBaseDir() {
        return logBaseDir;
    }

    /**
     * Get the log file output type.
     *
     * @return log output type, "file" by default
     */
    public static String getLogOutputType() {
        return logOutputType;
    }

    /**
     * Get the log file charset.
     *
     * @return the log file charset, "utf-8" by default
     */
    public static String getLogCharset() {
        return logCharSet;
    }

}
