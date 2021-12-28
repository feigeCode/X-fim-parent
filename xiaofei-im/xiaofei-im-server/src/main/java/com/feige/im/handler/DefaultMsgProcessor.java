package com.feige.im.handler;

import com.feige.im.constant.ChannelAttr;
import com.feige.im.constant.ProcessorEnum;
import com.feige.im.group.MyChannelGroup;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * @author feige<br />
 * @ClassName: DefaultMsgProcessor <br/>
 * @Description: <br/>
 * @date: 2021/10/10 13:24<br/>
 */
public class DefaultMsgProcessor implements MsgProcessor {
    private static final Logger LOG = LogManager.getLogger(DefaultMsgProcessor.class);
    private static final MyChannelGroup channelGroup = MyChannelGroup.getInstance();

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
                if (message instanceof DefaultMsg.Msg){
                    DefaultMsg.Msg msg = Parser.getT(DefaultMsg.Msg.class, message);
                    String receiverId = msg.getReceiverId();
                    channelGroup.write(receiverId,msg);
                }

                if (message instanceof DefaultMsg.Auth){
                    DefaultMsg.Auth msg = Parser.getT(DefaultMsg.Auth.class, message);
                    channel.attr(ChannelAttr.USER_ID).set(msg.getUserId());
                    channel.attr(ChannelAttr.PLATFORM).set(msg.getPlatform());
                    channel.attr(ChannelAttr.DEVICE_ID).set(msg.getDeviceId());
                    channel.attr(ChannelAttr.LANGUAGE).set(msg.getLanguage());
                    // 控制其他设备下线
                    Collection<Channel> channels = channelGroup.getChannels(msg.getUserId(), msg.getPlatform());
                    if (!channels.isEmpty()) {
                        channels.forEach(ch -> {
                            DefaultMsg.Forced forced = DefaultMsg.Forced.newBuilder()
                                    .setId(1L)
                                    .setUserId(msg.getUserId())
                                    .setIp("127.0.0.1")
                                    .setAddress("四川省成都市双流区")
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
}
