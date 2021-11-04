package com.feige.im.utils;

import com.sun.istack.internal.Nullable;

/**
 * @author feige<br />
 * @ClassName: StringUtil <br/>
 * @Description: <br/>
 * @date: 2021/10/6 21:54<br/>
 */
public class StringUtil {

    public static boolean isEmpty(@Nullable Object str) {
        return str == null || "".equals(str);
    }
}
