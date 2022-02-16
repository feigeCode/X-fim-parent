package com.feige.im.sender;

import com.google.protobuf.Message;
import io.netty.channel.Channel;

/**
 * @author feige<br />
 * @ClassName: PushManager <br/>
 * @Description: <br/>
 * @date: 2022/2/6 22:13<br/>
 */
public class PushManager {

    private static Channel channel;

    private PushManager(){

    }

    public static void setChannel(Channel channel){
        PushManager.channel = channel;
    }

    public static boolean pushMsg(Message message){
        if (channel == null || !channel.isActive() || !channel.isOpen()) {
            return false;
        }
        channel.writeAndFlush(message);
        return true;
    }
}
