package com.feige.im.service;

import com.google.protobuf.Message;
import io.netty.channel.Channel;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: ImBusinessService <br/>
 * @Description: <br/>
 * @date: 2021/12/31 9:39<br/>
 */
public interface ImBusinessService {

    /**
     * 持久化消息
     * @param msg
     * @return
     */
    long persistentMsg(Message msg);

    /**
     * 通过群组ID获取群成员
     * @param GroupId
     * @return
     */
    List<String> getUserIdsByGroupId(String GroupId);

    /**
     * 认证，用户和channel进行绑定
     * @param channel 用户的channel
     * @param auth 认证信息
     */
    void authenticate(Channel channel, Message auth);


}
