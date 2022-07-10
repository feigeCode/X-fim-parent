package com.feige.im.handler;

import com.feige.im.constant.ChannelAttr;
import com.feige.im.group.DefaultChannelContainer;
import com.feige.im.group.IChannelContainer;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.service.ImBusinessService;
import com.feige.im.service.impl.ImBusinessServiceImpl;
import com.feige.im.utils.ScheduledThreadPoolExecutorUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;
import java.util.Objects;

/**
 * @author feige<br />
 * @ClassName: DefaultMsgListener <br/>
 * @Description: <br/>
 * @date: 2022/1/21 13:59<br/>
 */
public class DefaultMsgListener extends  AbstractMsgListener{

    private static final Logger LOG = LoggerFactory.getLogger();
    private static final IChannelContainer CHANNEL_GROUP = DefaultChannelContainer.getInstance();
    private ImBusinessService imBusinessService;
    private static final ScheduledThreadPoolExecutorUtil EXECUTOR = ScheduledThreadPoolExecutorUtil.getInstance();;

    public DefaultMsgListener(ImBusinessService imBusinessService) {
        this.imBusinessService = imBusinessService;
    }

    public DefaultMsgListener() {
        this.imBusinessService = new ImBusinessServiceImpl();
    }

    @Override
    public void onActive(ChannelHandlerContext ctx) {
        ctx.channel().attr(ChannelAttr.ID).set(ctx.channel().id().asShortText());
    }

    @Override
    public void onReceivedMsg(ChannelHandlerContext ctx, Object message) {
        Channel channel = ctx.channel();
        if (Objects.isNull(message)) {
            return;
        }

        if (message instanceof DefaultMsg.Auth){
            imBusinessService.authenticate(channel,message);
            return;
        }

        EXECUTOR.execute(() -> {
            imBusinessService.persistentMsg(message);
            List<String> receiverIds = Parser.getReceiverIds(message, imBusinessService);
            receiverIds.forEach(receiverId -> {
                if (!receiverId.equals(channel.attr(ChannelAttr.USER_ID).get())){
                    CHANNEL_GROUP.write(receiverId,message);
                }
            });
        });
    }

    @Override
    public void onReceivedRequest(ChannelHandlerContext ctx, FullHttpRequest req) {

    }

    @Override
    public void onInactive(ChannelHandlerContext ctx) {
        LOG.info("channelId = {}的连接不活跃",ctx.channel().attr(ChannelAttr.ID));
        CHANNEL_GROUP.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.error(cause.getMessage(),cause);
    }


    public ImBusinessService getImBusinessService() {
        return imBusinessService;
    }

    public void setImBusinessService(ImBusinessService imBusinessService) {
        this.imBusinessService = imBusinessService;
    }
}
