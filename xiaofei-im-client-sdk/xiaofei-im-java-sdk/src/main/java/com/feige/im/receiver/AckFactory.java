package com.feige.im.receiver;

import com.feige.im.pojo.proto.Ack;
import com.feige.im.utils.AssertUtil;
import com.feige.im.utils.SnowflakeIdUtil;

/**
 * @author feige<br />
 * @ClassName: ProcessMsg <br/>
 * @Description: <br/>
 * @date: 2022/2/5 21:18<br/>
 */
public class AckFactory {

    private static String senderId;

    public static String getSenderId() {
        AssertUtil.notNull(senderId, "senderId");
        return senderId;
    }

    public static void setSenderId(String senderId) {
        AssertUtil.notNull(senderId, "senderId");
        AckFactory.senderId = senderId;
    }


    public static Ack.AckMsg buildAckMsg(Long msgId, String receiverId, Ack.AckMsg.Status status){
        return Ack.AckMsg.newBuilder()
                .setAckMsgId(msgId)
                .setId(SnowflakeIdUtil.generateId())
                .setMsgType(Ack.AckMsg.MsgType.PRIVATE)
                .setStatus(status)
                .setSenderId(getSenderId())
                .setReceiverId(receiverId)
                .build();
    }


    /**
     * 获取已发送的ack
     * @param msgId 消息ID
     * @param receiverId 接收者ID
     * @return
     */
    public static Ack.AckMsg getHaveSentAck(Long msgId, String receiverId){
        return buildAckMsg(msgId,receiverId, Ack.AckMsg.Status.HAVE_SENT);
    }

    /**
     * 获取已送达的ack
     * @param msgId 消息ID
     * @param receiverId 接收者ID
     * @return
     */
    public static Ack.AckMsg getArrivedAck(Long msgId, String receiverId){
        return buildAckMsg(msgId,receiverId, Ack.AckMsg.Status.ARRIVED);
    }

    /**
     * 获取已读的ack
     * @param msgId 消息ID
     * @param receiverId 接收者ID
     * @return
     */
    public static Ack.AckMsg getHaveReadAck(Long msgId, String receiverId){
        return buildAckMsg(msgId,receiverId, Ack.AckMsg.Status.HAVE_READ);
    }

}
