package com.feige.im.handler;

import com.feige.im.constant.ChannelAttr;
import com.feige.im.constant.ProcessorEnum;
import com.feige.im.group.MyChannelGroup;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.service.ImBusinessService;
import com.feige.im.service.impl.ImBusinessServiceImpl;
import com.feige.im.utils.NameThreadFactory;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;


/**
 * @author feige<br />
 * @ClassName: DefaultMsgProcessor <br/>
 * @Description: <br/>
 * @date: 2021/10/10 13:24<br/>
 */
public class DefaultMsgProcessor implements MsgProcessor {
    private static final Logger LOG = LogManager.getLogger(DefaultMsgProcessor.class);
    private static final MyChannelGroup channelGroup = MyChannelGroup.getInstance();
    private ImBusinessService imBusinessService;
    private ScheduledExecutorService executor;

    public DefaultMsgProcessor(ImBusinessService imBusinessService) {
        this.imBusinessService = imBusinessService;
        this.executor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() / 2,new NameThreadFactory("im-business-task-"));
    }

    public DefaultMsgProcessor() {
        this.imBusinessService = new ImBusinessServiceImpl();
    }

    @Override
    public void process(ProcessorEnum key, Channel channel, Message message, Throwable cause) {
        LOG.info("key={},channelId={},msg={}",() -> key,() -> channel.id().asShortText(),() -> StringUtil.protoMsgFormat(message));
        switch (key){
            case ACTIVE:
                LOG.info("有新的连接进入ShortId={}",channel.id().asShortText());
                break;
            case READ:
                if (Objects.isNull(message)) {
                    return;
                }
                // 消息持久化
                if (executor != null){
                    executor.execute(() -> {
                        imBusinessService.persistentMsg(message);
                    });
                }
                if (message instanceof DefaultMsg.TransportMsg){
                    DefaultMsg.TransportMsg transportMsg = Parser.getT(DefaultMsg.TransportMsg.class, message);
                    DefaultMsg.Msg msg = transportMsg.getMsg();
                    int type = transportMsg.getType();
                    String receiverId = msg.getReceiverId();
                    if (type == 1){
                        channelGroup.write(receiverId,transportMsg);
                    }else if (type == 2){
                        // 群消息转发
                        if (executor != null){
                            executor.execute(() -> sentGroupMsg(receiverId,transportMsg));
                        }else {
                            sentGroupMsg(receiverId,transportMsg);
                        }
                    }
                }
                if (message instanceof DefaultMsg.Auth){
                    DefaultMsg.Auth msg = Parser.getT(DefaultMsg.Auth.class, message);
                    channel.attr(ChannelAttr.USER_ID).set(msg.getToken());
                    channel.attr(ChannelAttr.PLATFORM).set(msg.getPlatform());
                    channel.attr(ChannelAttr.DEVICE_ID).set(msg.getDeviceId());
                    channel.attr(ChannelAttr.LANGUAGE).set(msg.getLanguage());
                    // 控制其他设备下线
                    Collection<Channel> channels = channelGroup.getChannels(msg.getToken(), msg.getPlatform());
                    if (!channels.isEmpty()) {
                        channels.forEach(ch -> {
                            DefaultMsg.Forced forced = DefaultMsg.Forced.newBuilder()
                                    .setIp(msg.getIp())
                                    .setAddress(msg.getAddress())
                                    .setContent("你的账号已在另一个地方登录")
                                    .setDeviceName(msg.getDeviceName())
                                    .setTimestamp(String.valueOf(new Date().getTime()))
                                    .build();
                            ch.writeAndFlush(forced);
                            ch.close();
                        });
                    }
                    channelGroup.add(channel);
                }
                break;
            case INACTIVE:
                channelGroup.remove(channel);
                break;
            case EXCEPTION:
                LOG.error("发生异常：",cause);
                break;
        }


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
