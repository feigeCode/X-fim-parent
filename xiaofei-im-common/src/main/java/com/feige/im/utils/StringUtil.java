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


    public static String protoMsgFormat(Message message){
        if (isEmpty(message) || isEmpty(message.toString())){
            return EMPTY_STR;
        }
        String msg = message.toString().trim().replaceAll("\n", ",");
        return "{" + msg + "}";
    }
}
