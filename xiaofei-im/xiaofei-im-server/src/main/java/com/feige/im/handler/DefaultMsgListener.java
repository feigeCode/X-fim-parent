package com.feige.im.handler;

import com.feige.im.constant.ChannelAttr;
import com.feige.im.group.MyChannelGroup;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.Ack;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.service.ImBusinessService;
import com.feige.im.service.impl.ImBusinessServiceImpl;
import com.feige.im.utils.ScheduledThreadPoolExecutorUtil;
import com.google.protobuf.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Objects;

/**
 * @author feige<br />
 * @ClassName: DefaultMsgListener <br/>
 * @Description: <br/>
 * @date: 2022/1/21 13:59<br/>
 */
public class DefaultMsgListener implements MsgListener{

    private static final Logger LOG = LoggerFactory.getLogger();
    private static final MyChannelGroup channelGroup = MyChannelGroup.getInstance();
    private ImBusinessService imBusinessService;
    private ScheduledThreadPoolExecutorUtil executor;

    public DefaultMsgListener(ImBusinessService imBusinessService) {
        this.imBusinessService = imBusinessService;
        this.executor = ScheduledThreadPoolExecutorUtil.getInstance();
    }

    public DefaultMsgListener() {
        this.imBusinessService = new ImBusinessServiceImpl();
    }

    @Override
    public void active(ChannelHandlerContext ctx) {
        ctx.channel().attr(ChannelAttr.ID).set(ctx.channel().id().asShortText());
    }

    @Override
    public void read(ChannelHandlerContext ctx, Message message) {
        Channel channel = ctx.channel();
        if (Objects.isNull(message)) {
            return;
        }

        if (message instanceof Ack.AckMsg) {
            Ack.AckMsg ackMsg = Parser.getT(Ack.AckMsg.class, message);
            String receiverId = ackMsg.getReceiverId();
            channelGroup.write(receiverId,ackMsg);
        }
        if (message instanceof DefaultMsg.TransportMsg){
            DefaultMsg.TransportMsg transportMsg = Parser.getT(DefaultMsg.TransportMsg.class, message);
            DefaultMsg.Msg msg = transportMsg.getMsg();
            // 消息持久化
            if (executor != null){
                executor.execute(() -> imBusinessService.persistentMsg(message));
            }
            DefaultMsg.TransportMsg.MsgType type = transportMsg.getType();
            String receiverId = msg.getReceiverId();
            if (type == DefaultMsg.TransportMsg.MsgType.PRIVATE){
                channelGroup.write(receiverId,transportMsg);
            }else if (type == DefaultMsg.TransportMsg.MsgType.GROUP){
                // 群消息转发
                if (executor != null){
                    executor.execute(() -> sentGroupMsg(receiverId,transportMsg));
                }else {
                    sentGroupMsg(receiverId,transportMsg);
                }
            }
        }
        if (message instanceof DefaultMsg.Auth){
            imBusinessService.authenticate(channel,message);
        }
    }

    @Override
    public void inactive(ChannelHandlerContext ctx) {
        LOG.info("channelId = {}的连接不活跃",ctx.channel().attr(ChannelAttr.ID));
        channelGroup.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.error(cause.getMessage(),cause);
    }


    /**
     * 发送群消息
     * @param groupId 群ID
     * @param msg 消息
     */
    public void sentGroupMsg(String groupId, Message msg){
        List<String> userIds = imBusinessService.getUserIdsByGroupId(groupId);
        if (userIds != null){
            for (String userId : userIds) {
                if (groupId.equals(userId)){
                    continue;
                }
                channelGroup.write(userId,msg);
            }
        }
    }


    public ImBusinessService getImBusinessService() {
        return imBusinessService;
    }

    public void setImBusinessService(ImBusinessService imBusinessService) {
        this.imBusinessService = imBusinessService;
    }
}
