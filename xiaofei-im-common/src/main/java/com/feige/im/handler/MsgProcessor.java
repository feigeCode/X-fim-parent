package com.feige.im.handler;

import com.feige.im.constant.ProcessorEnum;
import com.google.protobuf.Message;
import io.netty.channel.Channel;

/**
 * @author feige<br />
 * @ClassName: MsgProcessor <br/>
 * @Description: <br/>
 * @date: 2021/10/9 10:16<br/>
 */
@FunctionalInterface
public interface MsgProcessor {

    /**
     * 消息处理
     * @param key 事件key
     * @param channel 用户的通道
     * @param msg 接收到的消息
     */
    void process(ProcessorEnum key, Channel channel, Message msg);


}
