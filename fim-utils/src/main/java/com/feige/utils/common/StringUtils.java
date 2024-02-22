package com.feige.utils.common;


import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

/**
 * @author feige<br />
 * @ClassName: StringUtils <br/>
 * @Description: <br/>
 * @date: 2021/10/6 21:54<br/>
 */
public class StringUtils {

    public static final String EMPTY_STR = "";
    public final static Joiner   commaJoiner = Joiner.on(",");
    public final static Splitter commaSplitter = Splitter.on(",").omitEmptyStrings();
    public final static Splitter originCommaSplitter = Splitter.on(",");

    public static boolean isEmpty(Object str) {
        return str == null || EMPTY_STR.equals(str);
    }
    public static boolean isNull(Object obj){
        return obj == null;
    }
    public static boolean isBlank(Object obj){
        if (obj instanceof CharSequence){
            return isBlank((CharSequence) obj);
        }else {
            return isNull(obj);
        }
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


    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

   
    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (isBlank(str)) {
            return str;
        }

        char baseChar = str.charAt(0);
        char updatedChar;
        if (capitalize) {
            updatedChar = Character.toUpperCase(baseChar);
        }
        else {
            updatedChar = Character.toLowerCase(baseChar);
        }
        if (baseChar == updatedChar) {
            return str;
        }

        char[] chars = str.toCharArray();
        chars[0] = updatedChar;
        return new String(chars);
    }
}
