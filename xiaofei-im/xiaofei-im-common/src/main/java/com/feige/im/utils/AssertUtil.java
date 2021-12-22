package com.feige.im.utils;

/**
 * @author feige<br />
 * @ClassName: AssertUtil <br/>
 * @Description: <br/>
 * @date: 2021/11/14 0:29<br/>
 */
public class AssertUtil {

    public static void check(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void check(boolean expression, String message, Object... args) {
        if (!expression) {
            throw new IllegalStateException(String.format(message, args));
        }
    }

    public static void check(boolean expression, String message, Object arg) {
        if (!expression) {
            throw new IllegalStateException(String.format(message, arg));
        }
    }

    public static void notNull(Object object, String name) {
        if (object == null) {
            throw new IllegalStateException(name + " is null");
        }
    }

    public static void notEmpty(CharSequence s, String name) {
        if (StringUtil.isEmpty(s)) {
            throw new IllegalStateException(name + " is empty");
        }
    }

    public static void notBlank(CharSequence s, String name) {
        if (StringUtil.isBlank(s)) {
            throw new IllegalStateException(name + " is blank");
        }
    }
}
