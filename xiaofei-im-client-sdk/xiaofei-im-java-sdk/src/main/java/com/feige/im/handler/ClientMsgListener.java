package com.feige.im.handler;

import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.Ack;
import com.feige.im.receiver.ProcessMsg;
import com.feige.im.sender.WaitingAckTimerHandler;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author feige<br />
 * @ClassName: ClientMsgListener <br/>
 * @Description: <br/>
 * @date: 2022/2/5 20:44<br/>
 */
public abstract class ClientMsgListener implements MsgListener{

    // 收到消息的最后一个ID
    private final AtomicLong LAST_ID = new AtomicLong();
    private final Map<Long, Message> MSG_CONTAINER = new ConcurrentHashMap<>();

    @Override
    public void active(ChannelHandlerContext ctx) {

    }

    @Override
    public void read(ChannelHandlerContext ctx, Message msg) {
        if (msg instanceof Ack.AckMsg) {
            Ack.AckMsg ackMsg = Parser.getT(Ack.AckMsg.class, msg);
            WaitingAckTimerHandler.remove(ackMsg.getAckMsgId());
        }else {
            ProcessMsg processMsg = getProcessMsg(msg);
            if (processMsg != null){
                processMsg.doArrivedAck(ctx);
            }
        }
    }

    @Override
    public void inactive(ChannelHandlerContext ctx) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

    }

    public abstract ProcessMsg getProcessMsg(Message msg);

}
