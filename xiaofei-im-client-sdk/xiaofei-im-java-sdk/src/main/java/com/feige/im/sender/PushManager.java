package com.feige.im.sender;

import com.google.protobuf.Message;
import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author feige<br />
 * @ClassName: PushManager <br/>
 * @Description: <br/>
 * @date: 2022/2/6 22:13<br/>
 */
public class PushManager {

    private static Channel channel;

    public static void setChannel(Channel channel){
        PushManager.channel = channel;
    }

    public static boolean push(Message message){
        if (channel == null || !channel.isActive() || !channel.isOpen()) {
            return false;
        }
        channel.writeAndFlush(message);
        return true;
    }
}
