package com.feige.im.listener;

import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
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
    public void msgHandler(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof DefaultMsg.TransportMsg){
            DefaultMsg.TransportMsg transportMsg = Parser.getT(DefaultMsg.TransportMsg.class, msg);
            DefaultMsg.Msg message = transportMsg.getMsg();
            String id = message.getId();
            // 判断消息是否重复发送
            boolean repeat = this.isRepeat(id);
            if (repeat){
                LOG.warn("接收到重复的消息msgId = {}",id);
            }else {
                LOG.info("收到来自{}的消息，内容为：{}", message.getSenderId(), message.getContent());
            }
            // 发送ack
            this.sendAck(id,message.getSenderId());
        }
    }
}
