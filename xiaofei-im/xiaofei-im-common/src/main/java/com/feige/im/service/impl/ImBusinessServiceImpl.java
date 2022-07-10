package com.feige.im.service.impl;

import com.feige.im.constant.ChannelAttr;
import com.feige.im.group.DefaultChannelContainer;
import com.feige.im.group.IChannelContainer;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.service.ImBusinessService;
import com.google.protobuf.Message;
import io.netty.channel.Channel;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author feige<br />
 * @ClassName: ImBusinessServiceImpl <br/>
 * @Description: <br/>
 * @date: 2021/12/31 14:23<br/>
 */
public class ImBusinessServiceImpl implements ImBusinessService {

    private static final IChannelContainer channelGroup = DefaultChannelContainer.getInstance();

    @Override
    public long persistentMsg(Object msg) {
        return 0;
    }

    @Override
    public List<String> getUserIdsByGroupId(String GroupId) {
        return Collections.emptyList();
    }

    @Override
    public void authenticate(Channel channel, Object auth) {
        DefaultMsg.Auth msg = Parser.getT(DefaultMsg.Auth.class, auth);
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


}
