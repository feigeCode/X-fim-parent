package com.feige.im.receiver;

import com.feige.im.pojo.proto.Ack;
import com.feige.im.utils.SnowflakeIdUtil;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.CompletableFuture;

/**
 * @author feige<br />
 * @ClassName: ProcessMsg <br/>
 * @Description: <br/>
 * @date: 2022/2/5 21:18<br/>
 */
public class ProcessMsg {

    private final Long msgId;

    private final String senderId;

    private final String receiverId;

    public ProcessMsg(Long msgId, String senderId, String receiverId) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public CompletableFuture<Void> sentAck(ChannelHandlerContext ctx, Ack.AckMsg.Status status){
        return CompletableFuture
                .runAsync(() -> ctx.writeAndFlush(getAckMsg(status)));
    }

    public Ack.AckMsg getAckMsg(Ack.AckMsg.Status status){
        return Ack.AckMsg.newBuilder()
                .setAckMsgId(this.msgId)
                .setId(SnowflakeIdUtil.generateId())
                .setMsgType(Ack.AckMsg.MsgType.PRIVATE)
                .setStatus(status)
                .setSenderId(this.senderId)
                .setReceiverId(this.receiverId)
                .build();
    }

    public void doArrivedAck(ChannelHandlerContext ctx){
        CompletableFuture<Void> future = sentAck(ctx, Ack.AckMsg.Status.ARRIVED);
        future.complete(null);
    }

    public void doHaveReadAck(ChannelHandlerContext ctx){
        CompletableFuture<Void> future = sentAck(ctx, Ack.AckMsg.Status.HAVE_READ);
        future.complete(null);
    }

}
