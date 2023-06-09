package com.feige.fim.api;

public interface MsgStatusListener {

    /**
     * 有消息已发送到服务器
     * @param msgId 消息ID
     */
    void hasMsgSent(String msgId);


    /**
     * 有消息已送达接收方
     * @param msgId 消息ID
     */
    void hasMsgArrived(String msgId);


    /**
     * 超时未收到ack的消息
     * @param msgId 消息ID
     */
    void timeoutMsg(String msgId);


    /**
     * 异常消息（网络或服务端下线等问题导致发送失败）
     * @param msgId 消息ID
     */
    void exceptionMsg(String msgId);
}
