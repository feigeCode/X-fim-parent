package com.feige.im.utils;

/**
 * @author feige<br />
 * @ClassName: OsUtil <br/>
 * @Description: <br/>
 * @date: 2021/10/9 10:30<br/>
 */
public class OsUtil {

    public static boolean isLinux(){
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("linux");
    }
}
