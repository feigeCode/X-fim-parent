package com.feige.im.listener;

import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.receiver.AckFactory;
import com.feige.im.receiver.MsgHandler;
import com.feige.im.sender.WaitingAckTimerHandler;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author feige<br />
 * @ClassName: DefaultClientMsgListener <br/>
 * @Description: <br/>
 * @date: 2022/2/26 13:26<br/>
 */
public class DefaultClientMsgListener extends ClientMsgListener{

    private static final Logger LOG = LoggerFactory.getLogger();


    @Override
    public void msgHandler(ChannelHandlerContext ctx, Message msg) {
        if (msg instanceof DefaultMsg.TransportMsg){
            DefaultMsg.TransportMsg transportMsg = Parser.getT(DefaultMsg.TransportMsg.class, msg);
            DefaultMsg.Msg message = transportMsg.getMsg();
            long id = message.getId();
            // 判断消息是否重复发送
            boolean repeat = MsgHandler.isRepeat(id);
            if (repeat){
                LOG.warn("接收到重复的消息msgId = {}",id);
            }else {
                LOG.info("收到来自{}的消息，内容为：{}", message.getSenderId(), message.getContent());
            }
            // 发送ack
            MsgHandler.sendAck(AckFactory.getArrivedAck(id,message.getSenderId()));
        }
    }



}
