package com.feige.api.session;

import com.feige.api.handler.RemotingException;
import com.feige.api.crypto.Cipher;
import com.feige.api.sc.FutureListener;
import com.feige.api.sc.Listener;

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
    default void write(Object msg) throws RemotingException{
        write(msg, null);
    }
    /**
     * 往会话中写数据
     * @param listener 监听
     * @param msg 消息
     */
    void write(Object msg, Listener listener)  throws RemotingException;


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
     * mark is bind client
     */
    void markBindClient();

    /**
     * is handshake
     * @return
     */
    boolean isHandshake();

    /**
     * mark is handshake
     */
    void markHandshake();

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
