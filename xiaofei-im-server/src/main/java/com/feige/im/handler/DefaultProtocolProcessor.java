package com.feige.im.handler;

import com.feige.im.constant.ChannelAttr;
import com.feige.im.constant.ProcessorEnum;
import com.feige.im.group.MyChannelGroup;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.google.protobuf.Message;
import io.netty.channel.Channel;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * @author feige<br />
 * @ClassName: DefaultProtocolProcessor <br/>
 * @Description: <br/>
 * @date: 2021/10/10 13:24<br/>
 */
public class DefaultProtocolProcessor  implements ProtocolProcessor{



    @Override
    public void process(ProcessorEnum key, Channel channel, Message message) {
        MyChannelGroup channelGroup = MyChannelGroup.getInstance();
        switch (key){
            case READ:
                if (Objects.isNull(message)) {
                    return;
                }
                if (message instanceof DefaultMsg.Msg){
                    DefaultMsg.Msg msg = Parser.getT(DefaultMsg.Msg.class, message);
                    System.out.println(msg);
                    String receiverId = msg.getReceiverId();
                    channelGroup.write(receiverId,msg);
                }

                if (message instanceof DefaultMsg.Auth){
                    DefaultMsg.Auth msg = Parser.getT(DefaultMsg.Auth.class, message);
                    System.out.println(msg);
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
        }


    }
}
