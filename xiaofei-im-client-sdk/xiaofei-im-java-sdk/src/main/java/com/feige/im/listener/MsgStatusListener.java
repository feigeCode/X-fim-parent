package com.feige.im.listener;

/**
 * @author feige<br />
 * @ClassName: MsgStatusListener <br/>
 * @Description: <br/>
 * @date: 2022/2/26 12:39<br/>
 */
public interface MsgStatusListener {

    /**
     * 有消息已发送到服务器
     * @param msgId 消息ID
     */
    void hasMsgSent(Long msgId);


    /**
     * 有消息已送达接收方
     * @param msgId 消息ID
     */
    void hasMsgArrived(Long msgId);


    /**
     * 超时未收到ack的消息
     * @param msgId 消息ID
     */
    void timeoutMsg(Long msgId);


    /**
     * 异常消息（网络或服务端下线等问题导致发送失败）
     * @param msgId 消息ID
     */
    void exceptionMsg(Long msgId);
}
