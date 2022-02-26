package com.feige.im.listener;

import com.feige.im.handler.MsgListener;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.Ack;
import com.feige.im.sender.WaitingAckTimerHandler;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;

import java.util.Objects;

/**
 * @author feige<br />
 * @ClassName: ClientMsgListener <br/>
 * @Description: <br/>
 * @date: 2022/2/5 20:44<br/>
 */
public abstract class ClientMsgListener implements MsgListener {



    @Override
    public void active(ChannelHandlerContext ctx) {

    }

    @Override
    public void read(ChannelHandlerContext ctx, Message msg) {
        if (msg instanceof Ack.AckMsg) {
            Ack.AckMsg ackMsg = Parser.getT(Ack.AckMsg.class, msg);
            WaitingAckTimerHandler.remove(ackMsg.getAckMsgId());
        }else {
            if(Objects.isNull(ctx) || Objects.isNull(msg)){
                return;
            }
            msgHandler(ctx, msg);
        }
    }

    @Override
    public void inactive(ChannelHandlerContext ctx) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

    }

    /**
     * 处理接收到的消息
     * @param ctx channel上下文
     * @param msg 接收到的消息
     */
    public abstract void msgHandler(ChannelHandlerContext ctx, Message msg);

}
