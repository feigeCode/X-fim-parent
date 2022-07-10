package com.feige.im.listener;

import com.feige.im.constant.ImConst;
import com.feige.im.handler.AbstractMsgListener;
import com.feige.im.handler.MsgListener;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.Ack;
import com.feige.im.pojo.proto.HeartBeat;
import com.feige.im.receiver.AckFactory;
import com.feige.im.sender.PushManager;
import com.feige.im.sender.WaitingAckTimerHandler;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Objects;

/**
 * @author feige<br />
 * @ClassName: ClientMsgListener <br/>
 * @Description: <br/>
 * @date: 2022/2/5 20:44<br/>
 */
public abstract class ClientMsgListener extends AbstractMsgListener {



    @Override
    public void onActive(ChannelHandlerContext ctx) {

    }

    @Override
    public void onReceivedMsg(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Ack.AckMsg) {
            Ack.AckMsg ackMsg = Parser.getT(Ack.AckMsg.class, msg);
            WaitingAckTimerHandler.remove(ackMsg.getAckMsgId());
        }else if(msg instanceof HeartBeat.Ping){
            ctx.writeAndFlush(ImConst.PONG_MSG);
        }else {
            if(Objects.isNull(ctx) || Objects.isNull(msg)){
                return;
            }
            msgHandler(ctx, msg);
        }
    }

    @Override
    public void onInactive(ChannelHandlerContext ctx) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

    }

    @Override
    public void onReceivedRequest(ChannelHandlerContext ctx, FullHttpRequest req) {

    }

    /**
     * 处理接收到的消息
     * @param ctx channel上下文
     * @param msg 接收到的消息
     */
    public abstract void msgHandler(ChannelHandlerContext ctx, Object msg);



    public boolean isRepeat(String msgId){
        return false;
    }


    public void sendAck(String msgId, String senderId){
        PushManager.push(AckFactory.getArrivedAck(msgId, senderId));
    }

}
