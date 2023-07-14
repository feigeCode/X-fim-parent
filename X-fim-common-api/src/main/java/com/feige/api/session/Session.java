package com.feige.api.session;

import com.feige.api.handler.RemotingException;
import com.feige.api.cipher.Cipher;

import java.net.InetSocketAddress;

public interface Session {
    /**
     * 获取session的唯一id
     * @return
     */
    String getId();

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
    void write(Object msg) throws RemotingException;


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
     * is bind client
     * @return
     */
    boolean isBindClient();

    /**
     * set is bind client
     */
    void setBindClient(boolean isBindClient);

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
    
    Cipher getCipher();
    
    void setCipher(Cipher cipher);

}
