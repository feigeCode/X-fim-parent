package com.feige.api.session;

import java.net.InetSocketAddress;

public interface ISession {

    /**
     * 获取本地地址
     * @return
     */
    InetSocketAddress getLocalAddress();


    /**
     * 获取远程地址
     * @return
     */
    InetSocketAddress getRemoteAddress();

    /**
     * 往会话中写数据
     *
     * @param msg
     */
    void write(Object msg);


    /**
     * 关闭会话
     */
    void close();


    /**
     * 会话是否关闭
     * @return
     */
    boolean isClosed();


    /**
     * 会话是否是连接状态
     *
     * @return connected
     */
    boolean isConnected();

    /**
     * 是否有对应属性
     *
     * @param key key.
     * @return
     */
    boolean hasAttr(String key);

    /**
     * 获取对应属性
     *
     * @param key key.
     * @return value.
     */
    Object getAttr(String key);

    /**
     * 设置对应属性
     *
     * @param key   key.
     * @param value value.
     */
    void setAttr(String key, Object value);

    /**
     * 移除对应属性
     *
     * @param key key.
     */
    void removeAttr(String key);

}
