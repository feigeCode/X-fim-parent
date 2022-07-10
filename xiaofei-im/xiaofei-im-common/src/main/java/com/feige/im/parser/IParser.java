package com.feige.im.parser;

import com.feige.im.codec.ProtocolHeadLenType;
import io.netty.buffer.ByteBuf;


public interface IParser {

    /**
     * 获取对应实体对象
     * @param clazz 实体类
     * @param msg 消息对象
     * @param <T> 泛型
     * @return
     */
    <T> T getObject(Class<T> clazz, Object msg);
    /**
     * buf转实体对象
     * @param buf buf对象
     * @param protocolHeadLenType 协议头长度枚举
     * @param msgLength 消息长度，用于申请byte[]
     * @param key 消息类型
     * @return
     */
    Object decode(ByteBuf buf, ProtocolHeadLenType protocolHeadLenType, int msgLength, int key);


    /**
     * 实体对象转buf
     * @param msg 消息对象
     * @param protocolHeadLenType 协议类型
     * @param key 消息类型
     * @return
     */
    ByteBuf encode(Object msg, ProtocolHeadLenType protocolHeadLenType, int key);


    /**
     * 解析器类型
     * @return
     */
    int getType();
}
