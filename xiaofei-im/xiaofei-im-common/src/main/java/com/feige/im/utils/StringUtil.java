package com.feige.im.utils;

import com.google.protobuf.Message;

/**
 * @author feige<br />
 * @ClassName: StringUtil <br/>
 * @Description: <br/>
 * @date: 2021/10/6 21:54<br/>
 */
public class StringUtil {

    public static final String EMPTY_STR = "";

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static boolean isBlank(CharSequence s) {
        if (s != null) {
            for (int i = 0; i < s.length(); ++i) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }

        }
        return true;
    }

    public static boolean containsBlanks(CharSequence s) {
        if (s != null) {
            for (int i = 0; i < s.length(); ++i) {
                if (Character.isWhitespace(s.charAt(i))) {
                    return true;
                }
            }

        }
        return false;
    }


    public static String printMsg(Object message){
        if (isEmpty(message) || isEmpty(message.toString())){
            return EMPTY_STR;
        }
        String msg = message.toString();
        if (message instanceof Message){
            msg = "{" + msg.trim().replaceAll("\n", ",") + "}";
        }
        return msg;
    }

    public static int[] toCodePoints(final CharSequence str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new int[0];
        }

        final String s = str.toString();
        final int[] result = new int[s.codePointCount(0, s.length())];
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            result[i] = s.codePointAt(index);
            index += Character.charCount(result[i]);
        }
        return result;
    }

    public static boolean isNotBlank(CharSequence sequence) {
        return !isBlank(sequence);
    }
}
