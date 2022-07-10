package com.feige.im.group;

import io.netty.channel.Channel;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

public interface IChannelContainer {

    /**
     * @description: 获取用户的唯一标识
     * @author: feige
     * @date: 2021/10/9 21:10
     * @param	channel	当前用户的通道
     * @return: java.lang.String
     */
    String getKey(Channel channel);

    /**
     * @description: 把channel保存到MAP中
     * @author: feige
     * @date: 2021/10/9 22:08
     * @param	channel
     * @return: void
     */
    void add(Channel channel);

    /**
     * @description: 移除channel
     * @author: feige
     * @date: 2021/10/9 22:09
     * @param	channel
     * @return: void
     */
    void remove(Channel channel);

    /**
     * @description: 通过用户的唯一标识获取对应平台的channel
     * @author: feige
     * @date: 2021/10/9 22:09
     * @param	key	用户唯一标识
     * @param	platform 平台标识
     * @return: java.util.Collection<io.netty.channel.Channel>
     */
    default Collection<Channel> getChannels(String key,String... platform) {
        return Collections.emptyList();
    };

    /**
     * @description: 获取用户的channel
     * @author: feige
     * @date: 2021/10/9 22:10
     * @param	key
     * @return: java.util.Collection<io.netty.channel.Channel>
     */
    Collection<Channel> getChannels(String key);

    /**
     * @description: 往对应key的通道中写数据
     * @author: feige
     * @date: 2021/10/9 22:11
     * @param	key
     * @param	msg
     * @return: void
     */
    void write(String key, Object msg);

    /**
     * @description: 通过用户唯一标识往符合断言的通道写数据
     * @author: feige
     * @date: 2021/10/9 22:12
     * @param	key 唯一标识
     * @param	msg 消息
     * @param	predicate 断言规则，用来筛选满足条件的channel
     * @return: void
     */
    default void write(String key, Object msg, Predicate<Channel> predicate){

    }

    /**
     * 是否包含key
     * @param key
     * @return
     */
    boolean containsKey(String key);
}
